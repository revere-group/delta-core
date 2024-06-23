package dev.revere.delta.feature.clan.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/21/2024
 */
public class ClanKickCommand extends BaseCommand {
    @Command(name = "clan.kick", inGameOnly = true, description = "Kick a player from your clan")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /clan kick <player>"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        if (!Delta.getInstance().getClanRepository().isPlayerInClan(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou are not in a clan."));
            return;
        }

        if (player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(CC.translate("&cYou cannot kick yourself."));
            return;
        }

        Delta.getInstance().getClanRepository().kickMember(player, target);
    }
}
