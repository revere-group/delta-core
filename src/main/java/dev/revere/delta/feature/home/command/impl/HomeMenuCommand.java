package dev.revere.delta.feature.home.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.home.menu.HomeMenu;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/18/2024
 */
public class HomeMenuCommand extends BaseCommand {
    @Command(name = "home.menu", description = "Main home command")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Delta.getInstance().getHomeRepository().getHomes(player).isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Homes available."));
            return;
        }

        new HomeMenu().openMenu(player);
    }
}
