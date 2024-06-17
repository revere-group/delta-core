package dev.revere.revsentials.feature.ore;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.service.ConfigService;
import dev.revere.revsentials.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class OreListener implements Listener {

    private final Map<UUID, Integer> minedDiamonds = new HashMap<>();
    private final Map<UUID, BukkitRunnable> delayTasks = new HashMap<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        FileConfiguration messagesConfig = Revsential.getInstance().getServiceManager().getService(ConfigService.class).getConfigByName("messages.yml");
        Material blockType = event.getBlock().getType();
        if ((blockType == Material.DIAMOND_ORE || blockType == Material.DEEPSLATE_DIAMOND_ORE || blockType == Material.ANCIENT_DEBRIS) && messagesConfig.getBoolean("staff.ore-notify.enabled")) {
            UUID playerId = player.getUniqueId();

            int currentMined = minedDiamonds.getOrDefault(playerId, 0) + 1;
            minedDiamonds.put(playerId, currentMined);

            if (delayTasks.containsKey(playerId)) {
                return;
            }

            BukkitRunnable delayTask = new BukkitRunnable() {
                @Override
                public void run() {
                    int totalMined = minedDiamonds.remove(playerId);
                    Bukkit.getServer().getOnlinePlayers().stream().filter(staff -> staff.hasPermission("revsentials.staff.alerts")).forEach(staff -> staff.sendMessage(CC.translate(messagesConfig.getString("staff.ore-notify.message")
                            .replace("%player%", player.getName())
                            .replace("%total%", String.valueOf(totalMined))
                            .replace("%ore%", blockType.name().toLowerCase().replace("_", " "))
                            .replace("%time%", String.valueOf(messagesConfig.getInt("staff.ore-notify.delay"))))));
                    delayTasks.remove(playerId);
                }
            };
            delayTasks.put(playerId, delayTask);
            delayTask.runTaskLater(Revsential.getInstance(), messagesConfig.getInt("staff.ore-notify.delay") * 20L);
        }
    }
}
