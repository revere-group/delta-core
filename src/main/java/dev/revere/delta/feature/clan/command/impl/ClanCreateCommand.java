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
public class ClanCreateCommand extends BaseCommand {
    @Command(name = "clan.create", inGameOnly = true, description = "Create a clan")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /clan create <clan>"));
            return;
        }

        String clanName = args[0];
        Delta.getInstance().getClanRepository().createClan(clanName, player);
    }
}
