package dev.revere.revsentials.feature.clan.command.impl;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.feature.clan.Clan;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/21/2024
 */
public class ClanSetHomeCommand extends BaseCommand {
    @Command(name = "clan.sethome", inGameOnly = true, description = "Set the home of your clan")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Clan clan = Revsential.getInstance().getClanRepository().getPlayerClan(player.getUniqueId());
        if (clan == null) {
            player.sendMessage(CC.translate("&cYou are not in a clan."));
            return;
        }

        Revsential.getInstance().getClanRepository().setHome(player);
    }
}
