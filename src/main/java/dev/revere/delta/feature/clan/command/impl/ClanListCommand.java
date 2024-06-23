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
 * @date 6/17/2024
 */
public class ClanListCommand extends BaseCommand {
    @Command(name = "clan.list", inGameOnly = true, description = "List all clans")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lClan List &f(" + Delta.getInstance().getClanRepository().getClans().size() + "&f)"));
        if (Delta.getInstance().getClanRepository().getClans().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Clans available."));
        }

        Delta.getInstance().getClanRepository().getClans().forEach(clan -> player.sendMessage(CC.translate("      &f● &b" + clan.getName() + " &7(" + clan.getMembers().size()+ ")")));
        player.sendMessage("");
    }
}
