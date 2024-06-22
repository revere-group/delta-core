package dev.revere.revsentials.feature.clan.command.impl;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/21/2024
 */
public class ClanInviteCommand extends BaseCommand {
    @Command(name = "clan.invite", inGameOnly = true, description = "Invite a player to your clan")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /clan invite <player>"));
            return;
        }

        if (Revsential.getInstance().getClanRepository().getPlayerClan(player.getUniqueId()) == null) {
            player.sendMessage(CC.translate("&cYou are not in a clan."));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        Revsential.getInstance().getClanRepository().inviteToClan(player, target);
    }
}
