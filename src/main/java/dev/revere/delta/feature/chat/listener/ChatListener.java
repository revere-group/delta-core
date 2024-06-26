package dev.revere.delta.feature.chat.listener;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.chat.filter.FilterService;
import dev.revere.delta.feature.punishment.Punishment;
import dev.revere.delta.feature.punishment.PunishmentType;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Remi
 * @project Delta
 * @date 6/19/2024
 */
public class ChatListener implements Listener {

    /**
     * Handle the chat format
     *
     * @param event the event to handle
     */
    @EventHandler
    public void handleChatFormat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = getProfile(player);
        FileConfiguration config = getMessagesConfig();

        String chatFormat = formatChatMessage(config, profile, player, event.getMessage());
        event.setFormat(chatFormat);
    }

    /**
     * Handle the chat filter
     *
     * @param event the event to handle
     */
    @EventHandler
    public void handleChatFilter(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (shouldFilterMessage(player, message)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePunishmentChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = getProfile(player);

        if (profile.getPunishments().stream().anyMatch(punishment -> punishment.getType() == PunishmentType.MUTE && punishment.isActive())) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cYou are currently muted."));
        }
    }

    /**
     * Get the profile of a player
     *
     * @param player the player to get the profile of
     * @return the profile of the player
     */
    private Profile getProfile(Player player) {
        return Delta.getInstance()
                .getServiceManager()
                .getService(ProfileService.class)
                .getProfile(player.getUniqueId());
    }

    /**
     * Get the messages configuration
     *
     * @return the messages configuration
     */
    private FileConfiguration getMessagesConfig() {
        return Delta.getInstance()
                .getServiceManager()
                .getService(ConfigService.class)
                .getConfig("messages.yml");
    }

    /**
     * Format a chat message
     *
     * @param config  the messages configuration
     * @param profile the profile of the player
     * @param player  the player sending the message
     * @param message the message to format
     * @return the formatted message
     */
    private String formatChatMessage(FileConfiguration config, Profile profile, Player player, String message) {
        boolean translate = player.hasPermission("delta.chat.color");
        String chatFormat = CC.translate(config.getString("chat.format"));

        chatFormat = chatFormat.replace("%color%", profile.getNameColor());
        chatFormat = chatFormat.replace("%player%", player.getDisplayName());
        chatFormat = chatFormat.replace("%message%", translate ? CC.translate(message) : message);
        chatFormat = chatFormat.replace("%clan%", getClanTag(player));
        chatFormat = chatFormat.replace("%rank%", getRankPrefix(profile));
        chatFormat = chatFormat.replace("%suffix%", getRankSuffix(profile));
        chatFormat = chatFormat.replace("%world%", player.getWorld().getName());
        chatFormat = chatFormat.replace("%", "%%");

        return chatFormat;
    }

    /**
     * Get the clan tag of a player
     *
     * @param player the player to get the clan tag of
     * @return the clan tag of the player
     */
    private String getClanTag(Player player) {
        String clanTag = "";
        if (Delta.getInstance().getClanRepository().getPlayerClan(player.getUniqueId()) != null) {
            clanTag = "&f[" + Delta.getInstance().getClanRepository().getPlayerClan(player.getUniqueId()).getColoredName() + "&f] ";
        }
        return CC.translate(clanTag);
    }

    /**
     * Get the rank prefix of a player
     *
     * @param profile the profile of the player
     * @return the rank prefix of the player
     */
    private String getRankPrefix(Profile profile) {
        return CC.translate(Delta.getInstance()
                .getServiceManager()
                .getService(RankService.class)
                .getHighestRank(profile)
                .getPrefix());
    }

    /**
     * Get the rank suffix of a player
     *
     * @param profile the profile of the player
     * @return the rank prefix of the player
     */
    private String getRankSuffix(Profile profile) {
        return CC.translate(Delta.getInstance()
                .getServiceManager()
                .getService(RankService.class)
                .getHighestRank(profile)
                .getSuffix());
    }

    /**
     * Check if a message should be filtered
     *
     * @param player  the player sending the message
     * @param message the message to check
     * @return true if the message should be filtered, false otherwise
     */
    private boolean shouldFilterMessage(Player player, String message) {
        return Delta.getInstance()
                .getServiceManager()
                .getService(FilterService.class)
                .runCheck(player, message);
    }
}
