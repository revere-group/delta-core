package dev.revere.revsentials.feature.staff;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.service.ConfigService;
import dev.revere.revsentials.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;
import java.util.Objects;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class StaffListener implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String[] args = message.split(" ");
        String command = args[0].toLowerCase().substring(1);

        FileConfiguration config = Revsential.getInstance().getServiceManager().getService(ConfigService.class).getConfigByName("messages.yml");

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

    /**
     * Broadcast a message to all staff members
     *
     * @param message the message to broadcast
     */
    private void broadcastToStaff(String message) {
        Bukkit.getOnlinePlayers().stream().filter(onlinePlayer -> onlinePlayer.hasPermission("revsentials.staff.logger")).forEach(onlinePlayer -> onlinePlayer.sendMessage(CC.translate(message)));
    }

}
