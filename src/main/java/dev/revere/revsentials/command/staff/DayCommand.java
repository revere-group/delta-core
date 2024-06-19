package dev.revere.revsentials.command.staff;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/19/2024
 */
public class DayCommand extends BaseCommand {
    @Command(name = "day", permission = "revsentials.staff.day", description = "Change the time to day", usage = "/day")
    @Override
    public void onCommand(CommandArgs command) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Sleeping through this night")));

        Bukkit.getScheduler().runTaskLater(Revsential.getInstance(), () -> {
            Bukkit.getWorlds().forEach(world -> world.setTime(0));
        }, 60L);
    }
}
