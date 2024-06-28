package dev.revere.delta.feature.home;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.cooldown.Cooldown;
import dev.revere.delta.feature.combat.CombatLogService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.feature.cooldown.CooldownService;
import dev.revere.delta.util.CC;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class HomeRepository {

    private static final Pattern VALID_HOME_NAME = Pattern.compile("^[a-zA-Z0-9]+$");

    private final Map<String, Map<String, Home>> playerHomes;
    private final FileConfiguration homesConfig;

    public HomeRepository() {
        this.homesConfig = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("storage/homes.yml");
        this.playerHomes = new HashMap<>();

        loadHomes();
    }

    /**
     * Load all homes from the config
     */
    private void loadHomes() {
        if (homesConfig.contains("homes")) {
            Set<String> playerKeys = homesConfig.getConfigurationSection("homes").getKeys(false);

            for (String playerKey : playerKeys) {
                Set<String> homeKeys = homesConfig.getConfigurationSection("homes." + playerKey).getKeys(false);
                Map<String, Home> homes = new HashMap<>();

                for (String homeKey : homeKeys) {
                    String homeName = homesConfig.getString("homes." + playerKey + "." + homeKey + ".name");
                    Location homeLocation = homesConfig.getLocation("homes." + playerKey + "." + homeKey + ".location");
                    Home home = new Home(homeName, homeLocation);

                    assert homeName != null;
                    homes.put(homeName.toLowerCase(), home);
                }
                playerHomes.put(playerKey.toLowerCase(), homes);
            }
        }
    }

    /**
     * Save all homes to the config
     */
    public void saveHomes() {
        homesConfig.set("homes", null);

        for (String playerName : playerHomes.keySet()) {
            Map<String, Home> homes = playerHomes.get(playerName);
            for (String homeName : homes.keySet()) {
                Home home = homes.get(homeName);
                homesConfig.set("homes." + playerName.toLowerCase() + "." + homeName.toLowerCase() + ".name", home.getName());
                homesConfig.set("homes." + playerName.toLowerCase() + "." + homeName.toLowerCase() + ".location", home.getLocation());
            }
        }

        ConfigService configService = Delta.getInstance().getServiceManager().getService(ConfigService.class);
        configService.saveConfig(configService.getConfigFileByName("storage/homes.yml"), homesConfig);
    }

    /**
     * Remove a home from the config
     *
     * @param player   the player to remove the home from
     * @param homeName the name of the home
     */
    private void removeHomeFromConfig(Player player, String homeName) {
        String playerName = player.getName().toLowerCase();
        homesConfig.set("homes." + playerName + "." + homeName.toLowerCase(), null);

        ConfigService configService = Delta.getInstance().getServiceManager().getService(ConfigService.class);
        configService.saveConfig(configService.getConfigFileByName("storage/homes.yml"), homesConfig);
    }

    /**
     * Set a new home for a player
     *
     * @param player   the player to set the home for
     * @param homeName the name of the home
     */
    public void setHome(Player player, String homeName) {
        String playerName = player.getName().toLowerCase();
        Map<String, Home> homes = playerHomes.getOrDefault(playerName, new HashMap<>());

        if (!VALID_HOME_NAME.matcher(homeName).matches()) {
            player.sendMessage(CC.translate("&cHome names can only contain letters and numbers."));
            return;
        }

        if (homes.size() >= 7) {
            player.sendMessage(CC.translate("&cYou have reached the maximum limit of 7 homes."));
            return;
        }

        if (homes.containsKey(homeName.toLowerCase())) {
            player.sendMessage(CC.translate("&bEditing the location of home '&f" + homeName + "&b'."));
        }

        Location location = player.getLocation();
        Home home = new Home(homeName, location);
        homes.put(homeName.toLowerCase(), home);
        playerHomes.put(playerName, homes);
        saveHome(player, homeName.toLowerCase());
        player.sendMessage(CC.translate("&bHome '&f" + homeName + "&b' set successfully!"));
    }

    /**
     * Teleport a player to their home
     *
     * @param player   the player to teleport
     * @param homeName the name of the home
     */
    public void teleportHome(Player player, String homeName) {
        String playerName = player.getName().toLowerCase();
        Map<String, Home> homes = playerHomes.get(playerName);

        boolean inCombat = Delta.getInstance().getServiceManager().getService(CombatLogService.class).isPlayerInCombat(player);
        if (inCombat) {
            player.sendMessage(CC.translate("&cYou cannot teleport while in combat."));
            return;
        }

        if (homes == null || homes.get(homeName.toLowerCase()) == null) {
            player.sendMessage(CC.translate("&cYou don't have a home named '&f" + homeName + "&c'."));
            return;
        }

        CooldownService cooldownService = Delta.getInstance().getServiceManager().getService(CooldownService.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), "HOME_TELEPORT"));

        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before teleporting again."));
            return;
        }

        Home home = homes.get(homeName.toLowerCase());
        if (home != null) {
            player.teleport(home.getLocation());
            player.sendMessage(CC.translate("&bTeleported to home '&f" + home.getName() + "&b'."));
        }

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(60 * 1000L, null);
            cooldownService.addCooldown(player.getUniqueId(), "HOME_TELEPORT", newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();
    }

    /**
     * Save a home to the config
     *
     * @param player   the player to save the home for
     * @param homeName the name of the home
     */
    private void saveHome(Player player, String homeName) {
        String playerName = player.getName().toLowerCase();
        Home home = playerHomes.get(playerName).get(homeName.toLowerCase());

        homesConfig.set("homes." + playerName + "." + homeName.toLowerCase() + ".name", home.getName());
        homesConfig.set("homes." + playerName + "." + homeName.toLowerCase() + ".location", home.getLocation());

        ConfigService configService = Delta.getInstance().getServiceManager().getService(ConfigService.class);
        configService.saveConfig(configService.getConfigFileByName("storage/homes.yml"), homesConfig);
    }

    /**
     * List all the homes for a player
     *
     * @param player the player to list the homes for
     */
    public void listHomes(Player player) {
        String playerName = player.getName().toLowerCase();
        Map<String, Home> homes = playerHomes.get(playerName);

        if (homes != null && !homes.isEmpty()) {
            player.sendMessage(CC.translate("&bYour Homes:"));
            for (Home home : homes.values()) {
                player.sendMessage(CC.translate("&f- " + home.getName()));
            }
        } else {
            player.sendMessage(CC.translate("&cYou don't have any homes."));
        }
    }

    /**
     * Remove a home from a player
     *
     * @param player   the player to remove the home from
     * @param homeName the name of the home
     */
    public void removeHome(Player player, String homeName) {
        String playerName = player.getName().toLowerCase();
        Map<String, Home> homes = playerHomes.get(playerName);

        if (homes != null && homes.remove(homeName.toLowerCase()) != null) {
            playerHomes.put(playerName, homes);
            removeHomeFromConfig(player, homeName);
            player.sendMessage(CC.translate("&bHome '&f" + homeName + "&b' removed successfully!"));
        } else {
            player.sendMessage(CC.translate("&cYou don't have a home named '&f" + homeName + "&c'."));
        }
    }

    /**
     * Get all homes for a player
     *
     * @param player the player to get the homes for
     * @return a collection of homes
     */
    public Collection<Home> getHomes(Player player) {
        String playerName = player.getName().toLowerCase();
        Map<String, Home> homes = playerHomes.get(playerName);
        return homes != null ? homes.values() : new ArrayList<>();
    }
}
