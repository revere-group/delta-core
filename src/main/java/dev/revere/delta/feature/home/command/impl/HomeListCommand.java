package dev.revere.delta.feature.home.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class HomeListCommand extends BaseCommand {
    @Command(name = "home.list", inGameOnly = true, description = "List all homes")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lHome List &f(" + Delta.getInstance().getHomeRepository().getHomes(player).size() + "&f)"));
        if (Delta.getInstance().getHomeRepository().getHomes(player).isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Homes available."));
        }

        Delta.getInstance().getHomeRepository().getHomes(player).forEach(home -> player.sendMessage(CC.translate("      &f● &b" + home.getName() + " &7(" + home.getLocation().getBlockX() + " " + home.getLocation().getBlockY() + " " + home.getLocation().getBlockZ() + ")")));
        player.sendMessage("");
    }
}
