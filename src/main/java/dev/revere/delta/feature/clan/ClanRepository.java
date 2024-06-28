package dev.revere.delta.feature.clan;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.cooldown.Cooldown;
import dev.revere.delta.feature.combat.CombatLogService;
import dev.revere.delta.feature.cooldown.CooldownService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Remi
 * @project Delta
 * @date 6/21/2024
 */
public class ClanRepository {

    private static final Pattern VALID_CLAN_NAME = Pattern.compile("^[a-zA-Z0-9]+$");

    private final FileConfiguration clansConfig;
    private final Map<String, Clan> clans;

    public ClanRepository() {
        this.clansConfig = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("storage/clans.yml");
        this.clans = new HashMap<>();

        loadClans();
    }

    private void loadClans() {
        if (clansConfig.contains("clans")) {
            Set<String> clanNames = clansConfig.getConfigurationSection("clans").getKeys(false);

            for (String clanName : clanNames) {
                String leaderName = clansConfig.getString("clans." + clanName + ".leader");
                UUID leaderUUID = Bukkit.getOfflinePlayer(leaderName).getUniqueId();

                Clan clan = new Clan(clanName, leaderUUID);

                String membersString = clansConfig.getString("clans." + clanName + ".members");
                if (membersString != null && !membersString.isEmpty()) {
                    String[] memberNames = membersString.split(", ");
                    for (String memberName : memberNames) {
                        UUID memberUUID = Bukkit.getOfflinePlayer(memberName).getUniqueId();
                        clan.addMember(memberUUID);
                    }
                }

                String color = clansConfig.getString("clans." + clanName + ".color");
                if (color != null) {
                    clan.setColor(ChatColor.valueOf(color));
                } else {
                    clan.setColor(ChatColor.AQUA);
                }

                Location home = (Location) clansConfig.get("clans." + clanName + ".home");
                if (home != null) {
                    clan.setHome(home);
                }

                clans.put(clanName.toLowerCase(), clan);
            }
        }
    }

    public void saveClans() {
        clansConfig.set("clans", null);

        for (Clan clan : clans.values()) {
            String clanName = clan.getName();
            String leaderName = Bukkit.getOfflinePlayer(clan.getLeader()).getName();

            clansConfig.set("clans." + clanName + ".leader", leaderName);

            List<String> members = new ArrayList<>();
            for (UUID memberUUID : clan.getMembers()) {
                String memberName = Bukkit.getOfflinePlayer(memberUUID).getName();
                members.add(memberName);
            }

            String membersString = String.join(", ", members);

            clansConfig.set("clans." + clanName + ".color", clan.getColor().name());
            clansConfig.set("clans." + clanName + ".home", clan.getHome());
            clansConfig.set("clans." + clanName + ".members", membersString);
        }

        ConfigService configService = Delta.getInstance().getServiceManager().getService(ConfigService.class);
        configService.saveConfig(configService.getConfigFileByName("storage/clans.yml"), clansConfig);
    }

    /**
     * Create a new clan
     *
     * @param clanName the name of the clan
     * @param leader   the leader of the clan
     */
    public void createClan(String clanName, Player leader) {
        UUID leaderUUID = leader.getUniqueId();
        if (isPlayerInClan(leaderUUID)) {
            leader.sendMessage(CC.translate("&cYou are already in a clan."));
            return;
        }

        if (!VALID_CLAN_NAME.matcher(clanName).matches()) {
            leader.sendMessage(CC.translate("&cClan name can only contain letters and numbers."));
            return;
        }

        if (clanExists(clanName)) {
            leader.sendMessage(CC.translate("&cA clan with that name already exists."));
            return;
        }

        leader.sendMessage(CC.translate("&fClan '&b" + clanName + "&f' created successfully."));

        Clan newClan = new Clan(clanName, leaderUUID);
        clans.put(clanName.toLowerCase(), newClan);
        saveClans();
    }

    /**
     * Delete a clan
     *
     * @param deleter  the player deleting the clan
     */
    public void deleteClan(Player deleter) {
        String clanName = getPlayerClan(deleter.getUniqueId()).getName();
        if (clanName == null) {
            deleter.sendMessage(CC.translate("&cYou are not in a clan."));
            return;
        }

        Clan clan = getClan(clanName);

        UUID deleterUUID = deleter.getUniqueId();
        if (!clan.getLeader().equals(deleterUUID)) {
            deleter.sendMessage(CC.translate("&cYou are not the leader of clan '&f" + clanName + "&c'."));
            return;
        }

        clans.remove(clanName.toLowerCase());
        saveClans();
        deleter.sendMessage(CC.translate("&fClan '&b" + clanName + "&f' deleted successfully."));
    }

    /**
     * Set the color of a clan
     *
     * @param player   the player setting the color
     * @param color    the color to set
     */
    public void setColor(Player player, ChatColor color) {
        String clanName = getPlayerClan(player.getUniqueId()).getName();
        Clan clan = getClan(clanName);

        UUID playerUUID = player.getUniqueId();
        if (!clan.getLeader().equals(playerUUID)) {
            player.sendMessage(CC.translate("&cYou are not the leader of clan '&f" + clanName + "&c'."));
            return;
        }

        clan.setColor(color);
        saveClans();
        player.sendMessage(CC.translate("&fColor of clan '&b" + clanName + "&f' set to '&b" + color.name() + "&f'."));
    }

    /**
     * Set the home of a clan
     *
     * @param player   the player setting the home
     */
    public void setHome(Player player) {
        String clanName = getPlayerClan(player.getUniqueId()).getName();
        Clan clan = getClan(clanName);

        UUID playerUUID = player.getUniqueId();
        if (!clan.getLeader().equals(playerUUID)) {
            player.sendMessage(CC.translate("&cYou are not the leader of clan '&f" + clanName + "&c'."));
            return;
        }

        clan.setHome(player.getLocation());
        saveClans();
        player.sendMessage(CC.translate("&fHome of clan '&b" + clanName + "&f' set to '&b" + player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ() + "&f'."));
    }

    /**
     * Teleport to the home of a clan
     *
     * @param player   the player teleporting to the home
     */
    public void home(Player player) {
        String clanName = getPlayerClan(player.getUniqueId()).getName();
        Clan clan = getClan(clanName);

        boolean inCombat = Delta.getInstance().getServiceManager().getService(CombatLogService.class).isPlayerInCombat(player);
        if (inCombat) {
            player.sendMessage(CC.translate("&cYou cannot teleport while in combat."));
            return;
        }

        if (clan.getHome() == null) {
            player.sendMessage(CC.translate("&cClan '&f" + clanName + "&c' does not have a home set."));
            return;
        }

        CooldownService cooldownService = Delta.getInstance().getServiceManager().getService(CooldownService.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), "CLAN_HOME_TELEPORT"));

        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before teleporting again."));
            return;
        }

        player.teleport(clan.getHome());
        player.sendMessage(CC.translate("&fTeleported to the home of clan '&b" + clanName + "&f'."));

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(60 * 1000L, null);
            cooldownService.addCooldown(player.getUniqueId(), "CLAN_HOME_TELEPORT", newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();
    }


    /**
     * Invite a player to a clan
     *
     * @param inviter  the player inviting the other player
     * @param invitee  the player being invited
     */
    public void inviteToClan(Player inviter, Player invitee) {
        UUID inviteeUUID = invitee.getUniqueId();

        String clanName = getPlayerClan(inviter.getUniqueId()).getName();
        if (clanName == null) {
            inviter.sendMessage(CC.translate("&cYou are not in a clan."));
            return;
        }

        Clan clan = getClan(clanName);
        if (!clan.isLeader(inviter.getUniqueId())) {
            inviter.sendMessage(CC.translate("&cYou are not the leader of clan '&f" + clanName + "&c'."));
            return;
        }

        if (isPlayerInClan(inviteeUUID)) {
            inviter.sendMessage(CC.translate("&c" + invitee.getName() + " is already in a clan."));
            return;
        }

        clan.addInvite(inviteeUUID);
        saveClans();
        invitee.sendMessage(CC.translate("&fYou have been invited to join clan '&b" + clanName + "&f'."));
        clan.getMembers().stream().map(memberUUID -> Delta.getInstance().getServer().getPlayer(memberUUID)).filter(Objects::nonNull).forEach(member -> member.sendMessage(CC.translate("&b" + invitee.getName() + " &fhas been invited to join the clan.")));
    }

    /**
     * Kick a member from a clan
     *
     * @param leader   the player kicking the other player
     * @param member   the player being kicked
     */
    public void kickMember(Player leader, Player member) {
        String clanName = getPlayerClan(leader.getUniqueId()).getName();
        if (clanName == null) {
            leader.sendMessage(CC.translate("&cYou are not in a clan."));
            return;
        }

        Clan clan = getClan(clanName);

        UUID leaderUUID = leader.getUniqueId();
        UUID memberUUID = member.getUniqueId();

        if (!clan.getLeader().equals(leaderUUID)) {
            leader.sendMessage(CC.translate("&cYou are not the leader of clan '&f" + clanName + "&c'."));
            return;
        }

        if (!clan.isMember(memberUUID)) {
            leader.sendMessage(CC.translate("&cPlayer '&f" + member.getName() + "&c' is not a member of clan '&f" + clanName + "&c'."));
            return;
        }

        clan.removeMember(memberUUID);
        saveClans();
        member.sendMessage(CC.translate("&cYou have been kicked out of clan '&f" + clanName + "&c' by '&f" + leader.getName() + "&c'."));
        leader.sendMessage(CC.translate("&bYou have kicked '&f" + member.getName() + "&b' out of clan '&f" + clanName + "&b'."));
    }

    /**
     * Decline a clan invite
     *
     * @param player   the player declining the invite
     * @param clanName the name of the clan to decline the invite for
     */
    public void declineClanInvite(Player player, String clanName) {
        UUID playerUUID = player.getUniqueId();

        Clan clan = getClan(clanName);

        if (clan == null) {
            player.sendMessage(CC.translate("&cClan '&f" + clanName + "&c' does not exist."));
            return;
        }

        if (!clan.isInvited(playerUUID)) {
            player.sendMessage(CC.translate("&cYou have not been invited to join clan '&f" + clanName + "&c'."));
            return;
        }

        clan.removeInvite(playerUUID);
        saveClans();
        player.sendMessage(CC.translate("&bYou have declined the invitation to join clan '&f" + clanName + "&b'."));
        clan.getMembers().stream().map(memberUUID -> Delta.getInstance().getServer().getPlayer(memberUUID)).filter(Objects::nonNull).forEach(member -> member.sendMessage(CC.translate("&c" + player.getName() + " has declined the invitation to join the clan.")));
    }

    /**
     * Join a clan
     *
     * @param player   the player joining the clan
     * @param clanName the name of the clan to join
     */
    public void joinClan(Player player, String clanName) {
        UUID playerUUID = player.getUniqueId();

        Clan clan = getClan(clanName);

        if (clan.isMember(playerUUID)) {
            player.sendMessage(CC.translate("&cYou are already a member of clan '&f" + clanName + "&c'."));
            return;
        }

        if (!clan.isInvited(playerUUID)) {
            player.sendMessage(CC.translate("&cYou have not been invited to join clan '&f" + clanName + "&c'."));
            return;
        }

        clan.addMember(playerUUID);
        saveClans();
        player.sendMessage(CC.translate("&fYou have joined clan '&b" + clanName + "&f'."));
        clan.getMembers().stream().map(memberUUID -> Delta.getInstance().getServer().getPlayer(memberUUID)).filter(Objects::nonNull).forEach(member -> member.sendMessage(CC.translate("&b" + player.getName() + " &fhas joined the clan.")));
    }

    /**
     * Leave a clan
     *
     * @param player   the player leaving the clan
     */
    public void leaveClan(Player player) {
        String clanName = getPlayerClan(player.getUniqueId()).getName();
        if (clanName == null) {
            player.sendMessage(CC.translate("&cYou are not in a clan."));
            return;
        }

        UUID playerUUID = player.getUniqueId();
        Clan clan = getClan(clanName);
        if (!clan.isMember(playerUUID)) {
            player.sendMessage(CC.translate("&cYou are not a member of clan '&f" + clanName + "&c'."));
            return;
        }

        if (clan.getLeader().equals(playerUUID)) {
            player.sendMessage(CC.translate("&cYou are the leader of clan '&f" + clanName + "&c'."));
            return;
        }

        clan.removeMember(playerUUID);
        saveClans();
        player.sendMessage(CC.translate("&bYou have left clan '&f" + clanName + "&b'."));
        clan.getMembers().stream().map(memberUUID -> Delta.getInstance().getServer().getPlayer(memberUUID)).filter(Objects::nonNull).forEach(member -> member.sendMessage(CC.translate("&c" + player.getName() + " has left the clan.")));
    }

    /**
     * Broadcast a message to a clan
     *
     * @param clan    the clan to broadcast the message to
     * @param message the message to broadcast
     */
    public void broadcastClanMessage(Clan clan, String message) {
        clan.getMembers().stream()
                .map(memberUUID -> Bukkit.getServer().getPlayer(memberUUID))
                .filter(Objects::nonNull)
                .forEach(member -> member.sendMessage(CC.translate(message)));
    }

    /**
     * Get the clan of a player
     *
     * @param playerUUID the UUID of the player
     * @return the clan of the player, or null if the player is not in a clan
     */
    public Clan getPlayerClan(UUID playerUUID) {
        for (Clan clan : clans.values()) {
            if (clan.isLeader(playerUUID) || clan.isMember(playerUUID)) {
                return clan;
            }
        }
        return null;
    }

    /**
     * Check if a player is in a clan
     *
     * @param playerUUID the UUID of the player to check
     * @return true if the player is in a clan
     */
    public boolean isPlayerInClan(UUID playerUUID) {
        for (Clan clan : clans.values()) {
            if (clan.isMember(playerUUID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a clan exists
     *
     * @param clanName the name of the clan to check
     * @return true if the clan exists
     */
    public boolean clanExists(String clanName) {
        return clans.containsKey(clanName.toLowerCase());
    }

    /**
     * Get a clan by name
     *
     * @param clanName the name of the clan
     * @return the clan, or null if the clan does not exist
     */
    public Clan getClan(String clanName) {
        return clans.get(clanName.toLowerCase());
    }

    public Collection<Clan> getClans() {
        return clans.values();
    }
}