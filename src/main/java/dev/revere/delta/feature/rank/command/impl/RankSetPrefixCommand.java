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
public class RankSetPrefixCommand extends BaseCommand {
    @Command(name = "rank.setprefix", permission = "delta.rank.setprefix", inGameOnly = true, description = "Set a rank's prefix")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setprefix <name> <prefix>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        String prefix = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(name);
        rank.setPrefix(prefix);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&fSuccessfully set the prefix of the rank &b" + name + " &fto &b" + prefix + "&f."));
    }
}
