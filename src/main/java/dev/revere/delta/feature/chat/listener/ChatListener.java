package dev.revere.delta.feature.chat.listener;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.chat.filter.FilterService;
import dev.revere.delta.feature.punishment.PunishmentType;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
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
     * Handle the chat
     *
     * @param event the event to handle
     */
    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = getProfile(player);
        FileConfiguration config = getMessagesConfig();

        if (profile.getPunishments().stream().anyMatch(punishment -> punishment.getType() == PunishmentType.MUTE && !punishment.hasExpired())) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cYou are currently muted."));
            return;
        }

        if (shouldFilterMessage(player, event.getMessage())) {
            event.setCancelled(true);
            return;
        }

        String chatFormat = formatChatMessage(config, profile, player, event.getMessage());
        for (Player recipient : Bukkit.getOnlinePlayers()) {
            recipient.sendMessage(chatFormat);
        }

        event.setCancelled(true);
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
        boolean playerNameSmallFont = config.getBoolean("chat.player-name-small-font");
        boolean clanNameSmallFont = config.getBoolean("chat.clan-name-small-font");
        boolean rankSmallFont = config.getBoolean("chat.rank-prefix-small-font");

        String chatFormat = CC.translate(config.getString("chat.format"));
        String rank = getRankPrefix(profile) + " ";

        String rankName = rankSmallFont ? CC.toSmallFont(CC.translate(rank)) : rank;

        String playerName = playerNameSmallFont ? CC.toSmallFont(player.getDisplayName()) : player.getDisplayName();
        chatFormat = chatFormat.replace("%color%", profile.getNameColor());
        chatFormat = chatFormat.replace("%player%", playerName);
        chatFormat = chatFormat.replace("%message%", translate ? CC.translate(message) : message);
        chatFormat = chatFormat.replace("%rank%", rankName);
        chatFormat = chatFormat.replace("%tag%", getTagPrefix(profile));
        chatFormat = chatFormat.replace("%suffix%", getRankSuffix(profile));
        chatFormat = chatFormat.replace("%world%", player.getWorld().getName());
        chatFormat = chatFormat.replace("%ping%", String.valueOf(player.getPing()));
        chatFormat = chatFormat.replace("%coins%", String.valueOf(profile.getCoins()));

        return Delta.getInstance().isPlaceholderAPIEnabled() ? PlaceholderAPI.setPlaceholders(player, chatFormat) : chatFormat;
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
     * Get the tag prefix of a player
     *
     * @param profile the profile of the player
     * @return the tag prefix of the player
     */
    private String getTagPrefix(Profile profile) {
        return CC.translate(profile.getTag() != null ? " " + profile.getTag().getPrefix() : "");
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
