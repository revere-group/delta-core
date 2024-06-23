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
public class HomeDeleteCommand extends BaseCommand {
    @Command(name = "home.delete", inGameOnly = true, description = "Delete a home")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /home delete <homeName>"));
            return;
        }

        String homeName = args[0];
        Delta.getInstance().getHomeRepository().removeHome(player, homeName);
    }
}
