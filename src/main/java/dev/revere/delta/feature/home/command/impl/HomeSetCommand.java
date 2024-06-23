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
public class HomeSetCommand extends BaseCommand {
    @Command(name = "home.sethome", aliases = {"home.create", "home.set"}, inGameOnly = true, description = "Create a home")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /home set <homeName>"));
            return;
        }

        String homeName = args[0];
        Delta.getInstance().getHomeRepository().setHome(player, homeName);
    }
}
