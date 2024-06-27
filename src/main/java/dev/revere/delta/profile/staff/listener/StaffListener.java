package dev.revere.delta.profile.staff.listener;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.lang.Locale;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Objects;

/**
 * @author Remi
 * @project Delta
 * @date 6/19/2024
 */
public class StaffListener implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String[] args = message.split(" ");
        String command = args[0].toLowerCase().substring(1);

        FileConfiguration config = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml");

        if (config.getBoolean("staff.logger.enabled", false)) {
            List<String> monitoredCommands = config.getStringList("staff.logger.commands");

            if (monitoredCommands.contains(command)) {
                String formattedMessage = Objects.requireNonNull(config.getString("staff.logger.message"))
                        .replace("%player%", player.getName())
                        .replace("%command%", message);
                broadcastToStaff(formattedMessage);
            }
        }
    }

    @EventHandler
    private void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        handleVisibility(player);
    }

    @EventHandler
    private void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        boolean isStaffChat = profile.getStaffOptions().isStaffChat();
        boolean startsWith = event.getMessage().startsWith("@");
        boolean condition = (isStaffChat || startsWith) && player.hasPermission("delta.staff.chat");

        if (condition) {
            event.setCancelled(true);

            String message = event.getMessage();
            if (startsWith) {
                message = message.substring(1).trim();
            }

            FileConfiguration config = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml");
            String format = config.getString("staff.chat.format");
            String permission = "delta.staff.viewchat";

            if (format == null) {
                return;
            }

            String finalMessage = message;
            Bukkit.getOnlinePlayers().stream()
                    .filter(staff -> staff.hasPermission(permission))
                    .forEach(staff -> staff.sendMessage(CC.translate(format
                                    .replace("%rank-color%", String.valueOf(Delta.getInstance().getServiceManager().getService(RankService.class).getHighestRank(profile).getNameColor()))
                            .replace("%player%", player.getName())
                            .replace("%server%", Locale.SERVER_NAME)
                            .replace("%message%", finalMessage))));
        }
    }

    /**
     * Broadcast a message to all staff members
     *
     * @param message the message to broadcast
     */
    private void broadcastToStaff(String message) {
        Bukkit.getOnlinePlayers().stream().filter(onlinePlayer -> onlinePlayer.hasPermission("delta.staff.logger")).forEach(onlinePlayer -> onlinePlayer.sendMessage(CC.translate(message)));
    }

    /**
     * Handle the visibility of the player
     *
     * @param player the player
     */
    private void handleVisibility(Player player) {
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        if (profile.getStaffOptions().isVanish()) {
            player.setCanPickupItems(false);
            player.setCollidable(false);
            player.setInvulnerable(true);
            player.setAllowFlight(true);

            for (Player online : player.getServer().getOnlinePlayers()) {
                if (online.hasPermission("delta.staff.vanish")) {
                    online.showPlayer(player);
                } else {
                    online.hidePlayer(player);
                }
            }
        } else {
            player.setCanPickupItems(true);
            player.setCollidable(true);
            player.setInvulnerable(false);
            player.setAllowFlight(false);
            for (Player online : player.getServer().getOnlinePlayers()) {
                online.showPlayer(player);
            }
        }
    }
}
