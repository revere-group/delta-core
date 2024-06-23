package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class RankViewCommand extends BaseCommand {
    @Command(name = "rank.view", permission = "delta.rank.view", inGameOnly = true, description = "View a rank's information")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /rank view <name>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(name);
        player.sendMessage("");
        player.sendMessage(CC.translate("&b&l" + rank.getName() + " &7Information:"));
        player.sendMessage(CC.translate(" &f● &fName: &b" + rank.getName()));
        player.sendMessage(CC.translate(" &f● &fPrefix: &b" + rank.getPrefix()));
        player.sendMessage(CC.translate(" &f● &fSuffix: &b" + rank.getSuffix()));
        player.sendMessage(CC.translate(" &f● &fWeight: &b" + rank.getWeight()));
        player.sendMessage(CC.translate(" &f● &fStaff: &b" + rank.isStaffRank()));
        player.sendMessage(CC.translate(" &f● &fDefault: &b" + rank.isDefaultRank()));
        player.sendMessage(CC.translate(" &f● &fPermissions: &b" + rank.getPermissions().toString()));
        player.sendMessage("");

    }
}
