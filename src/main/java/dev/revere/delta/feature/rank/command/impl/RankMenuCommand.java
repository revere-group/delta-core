package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.home.menu.HomeMenu;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.feature.rank.menu.RankMenu;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/18/2024
 */
public class RankMenuCommand extends BaseCommand {
    @Command(name = "rank.menu", description = "Open the rank menu", inGameOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Delta.getInstance().getServiceManager().getService(RankService.class).getRanks().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Ranks available."));
            return;
        }

        new RankMenu().openMenu(player);
    }
}
