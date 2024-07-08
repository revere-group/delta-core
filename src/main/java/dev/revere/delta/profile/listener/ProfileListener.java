package dev.revere.delta.profile.listener;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.punishment.Punishment;
import dev.revere.delta.feature.punishment.PunishmentType;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.feature.server.ServerService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.DateUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class ProfileListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        Profile profile = new Profile(player.getUniqueId());
        profile.loadProfile();

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        profileService.addProfile(profile.getUuid(), profile);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void handlePunishmentLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        if (profile == null) {
            return;
        }

        if (profile.getPunishments().stream().anyMatch(punishment -> punishment.getType() == PunishmentType.BAN && !punishment.hasExpired())) {
            Punishment punishment = profile.getPunishments().stream().filter(pun -> pun.getType() == PunishmentType.BAN && !pun.hasExpired()).findFirst().get();
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, CC.translate("&cYou are currently banned for " + punishment.getReason() + ". Expiring in: " + (punishment.isPermanent() ? "Permanent" : DateUtils.formatTimeMillis(punishment.getRemainingTime()))));
        } else if (profile.getPunishments().stream().anyMatch(punishment -> punishment.getType() == PunishmentType.BLACKLIST && !punishment.hasExpired())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, CC.translate("&cYou are currently blacklisted."));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.setName(player.getName());
        profile.setOnline(true);

        ConfigService configService = Delta.getInstance().getServiceManager().getService(ConfigService.class);
        ServerService serverService = Delta.getInstance().getServiceManager().getService(ServerService.class);
        handleTeleportationToSpawn(configService, player, serverService);

        profileService.loadPermissions(player);

        handleWelcomeMessage(player, configService);
        handleJoinTitle(player, configService);

        if (profile.getStaffOptions().isVanish()) {
            event.setJoinMessage(null);
            return;
        }

        handleJoinMessage(event, player, configService);
    }

    /**
     * Handles the welcome message of a player
     *
     * @param player        the player to send the message to
     * @param configService the config service
     */
    private void handleWelcomeMessage(Player player, ConfigService configService) {
        if (configService.getConfig("messages.yml").getBoolean("on-join.messages.welcome-message.enabled", true)) {
            List<String> welcomeMessages = configService.getConfig("messages.yml").getStringList("on-join.messages.welcome-message.message");
            sendWelcomeMessage(player, welcomeMessages);
        }
    }

    /**
     * Handles the join title of a player
     *
     * @param player        the player to send the title to
     * @param configService the config service
     */
    private void handleJoinTitle(Player player, ConfigService configService) {
        if (configService.getConfig("messages.yml").getBoolean("on-join.title-sender.enabled")) {
            String mainTitle = configService.getConfig("messages.yml").getString("on-join.title-sender.main-title").replace("%player%", player.getName());
            String subTitle = configService.getConfig("messages.yml").getString("on-join.title-sender.sub-title").replace("%player%", player.getName());

            player.sendTitle(CC.translate(mainTitle), CC.translate(subTitle));
        }
    }

    /**
     * Handles the join message of a player
     *
     * @param event         the event
     * @param player        the player
     * @param configService the config service
     */
    private void handleJoinMessage(PlayerJoinEvent event, Player player, ConfigService configService) {
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());
        String joinMessage = configService.getConfig("messages.yml").getString("on-join.messages.joined-the-game.message")
                .replace("%player%", player.getName())
                .replace("%color%", String.valueOf(Delta.getInstance().getServiceManager().getService(RankService.class).getHighestRank(profile).getNameColor()));
        String firstJoinMessage = configService.getConfig("messages.yml").getString("on-join.messages.first-join.message").replace("%player%", player.getName());

        boolean joinMessageEnabled = configService.getConfig("messages.yml").getBoolean("on-join.messages.joined-the-game.enabled");
        boolean firstJoinMessageEnabled = configService.getConfig("messages.yml").getBoolean("on-join.messages.first-join.enabled");

        if (player.hasPlayedBefore()) {
            event.setJoinMessage(joinMessageEnabled ? CC.translate(joinMessage) : null);
        } else {
            event.setJoinMessage(firstJoinMessageEnabled ? CC.translate(firstJoinMessage) : null);
        }
    }

    /**
     * Handles the teleportation of a player to the spawn
     *
     * @param configService the config service
     * @param player        the player to teleport
     * @param serverService the server service
     */
    private void handleTeleportationToSpawn(ConfigService configService, Player player, ServerService serverService) {
        if (configService.getConfig("settings.yml").getBoolean("teleport-spawn.enabled")) {
            boolean onlyFirstJoin = configService.getConfig("settings.yml").getBoolean("teleport-spawn.only-first-join");

            if (!onlyFirstJoin || !player.hasPlayedBefore()) {
                player.teleport(serverService.getSpawnpoint(serverService.getServerName()));
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        ConfigService configService = Delta.getInstance().getServiceManager().getService(ConfigService.class);
        ServerService serverService = Delta.getInstance().getServiceManager().getService(ServerService.class);

        if (configService.getConfig("settings.yml").getBoolean("teleport-spawn.on-death.enabled")) {
            boolean onlyWithoutBed = configService.getConfig("settings.yml").getBoolean("teleport-spawn.on-death.only-without-bed");

            if (!onlyWithoutBed || player.getBedSpawnLocation() == null) {
                player.sendMessage("You have been teleported to spawn.");
                event.setRespawnLocation(serverService.getSpawnpoint(serverService.getServerName()));
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());
        profile.setOnline(false);
        profile.saveProfile();

        if (profile.getStaffOptions().isVanish()) {
            event.setQuitMessage(null);
            return;
        }

        String quitMessage = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("on-leave.messages.left-the-game")
                .replace("%player%", player.getName())
                .replace("%color%", String.valueOf(Delta.getInstance().getServiceManager().getService(RankService.class).getHighestRank(profile).getNameColor()));
        ;
        event.setQuitMessage(CC.translate(quitMessage));
    }

    /**
     * Sends the welcome message to the player
     *
     * @param player the player to send the message to
     */
    private void sendWelcomeMessage(Player player, List<String> messages) {
        for (String message : messages) {
            String replacedMessage = replaceWelcomeMessage(player, message);
            player.sendMessage(CC.translate(replacedMessage));
        }
    }

    /**
     * Replaces the placeholders in the welcome message
     *
     * @param player  the player to replace the placeholders for
     * @param message the message to replace the placeholders in
     * @return the message with the placeholders replaced
     */
    private String replaceWelcomeMessage(Player player, String message) {
        String bars = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("on-join.messages.bars-format");

        assert bars != null;
        return message
                .replace("%player%", player.getName())
                .replace("%health%", String.valueOf(Math.round(player.getHealth())))
                .replace("%bars%", bars);
    }
}
