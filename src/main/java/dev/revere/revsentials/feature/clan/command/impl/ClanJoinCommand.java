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
public class ClanJoinCommand extends BaseCommand {
    @Command(name = "clan.join", inGameOnly = true, description = "Join a clan")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /clan join <clanName>"));
            return;
        }

        String clanName = args[0];
        Clan clan = Revsential.getInstance().getClanRepository().getClan(clanName);
        if (clan == null) {
            player.sendMessage(CC.translate("&cThat clan does not exist."));
            return;
        }

        Revsential.getInstance().getClanRepository().joinClan(player, clanName);
    }
}
