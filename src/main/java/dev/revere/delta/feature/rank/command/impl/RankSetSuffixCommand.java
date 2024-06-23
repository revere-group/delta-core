package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class RankSetSuffixCommand extends BaseCommand {
    @Command(name = "rank.setsuffix", permission = "delta.rank.setsuffix", inGameOnly = true, description = "Set a rank's suffix")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setsuffix <name> <suffix>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        String suffix = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(name);
        rank.setSuffix(suffix);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&fSuccessfully set the suffix of the rank &b" + name + " &fto &b" + suffix + "&f."));
    }
}
