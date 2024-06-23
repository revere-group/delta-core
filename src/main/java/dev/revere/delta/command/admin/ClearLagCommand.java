package dev.revere.delta.command.admin;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class ClearLagCommand extends BaseCommand {
    @Command(name = "clearlag", permission = "delta.admin.clearlag", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        int countdownSeconds = 5;
        String messagePrefix = CC.translate("&bClearing all items from the ground in ");

        Bukkit.broadcastMessage(messagePrefix + countdownSeconds + " seconds...");

        Bukkit.getScheduler().runTaskLater(Delta.getInstance(), () -> {
            for (int i = countdownSeconds - 1; i > 0; i--) {
                final int count = i;
                Bukkit.getScheduler().runTaskLater(Delta.getInstance(), () -> {
                    Bukkit.broadcastMessage(messagePrefix + count + " seconds...");
                }, (countdownSeconds - i) * 20L);
            }

            Bukkit.getScheduler().runTaskLater(Delta.getInstance(), () -> {
                for (World world : Bukkit.getServer().getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof Item) {
                            entity.remove();
                        }
                    }
                }
                System.gc();
                player.sendMessage(CC.translate("&bCleared all items from the ground."));
            }, countdownSeconds * 20L);
        }, 0L);
    }
}
