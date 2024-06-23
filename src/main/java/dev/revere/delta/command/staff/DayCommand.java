package dev.revere.delta.command.staff;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

/**
 * @author Remi
 * @project Delta
 * @date 6/19/2024
 */
public class DayCommand extends BaseCommand {
    @Command(name = "day", permission = "delta.staff.day", description = "Change the time to day", usage = "/day")
    @Override
    public void onCommand(CommandArgs command) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Sleeping through this night")));

        Bukkit.getScheduler().runTaskLater(Delta.getInstance(), () -> {
            Bukkit.getWorlds().forEach(world -> world.setTime(0));
        }, 60L);
    }
}
