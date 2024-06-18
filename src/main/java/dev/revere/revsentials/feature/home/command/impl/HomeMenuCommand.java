package dev.revere.revsentials.feature.home.command.impl;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.feature.home.menu.HomeMenu;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/18/2024
 */
public class HomeMenuCommand extends BaseCommand {
    @Command(name = "home.menu", description = "Main home command")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Revsential.getInstance().getHomeRepository().getHomes(player).isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Homes available."));
            return;
        }

        new HomeMenu().openMenu(player);
    }
}
