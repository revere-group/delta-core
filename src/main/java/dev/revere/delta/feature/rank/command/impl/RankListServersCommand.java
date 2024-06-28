package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class RankListServersCommand extends BaseCommand {
    @Command(name = "rank.listservers", permission = "delta.rank.listservers", inGameOnly = true, description = "List rank servers")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /rank listservers <rank>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);
        String rankName = args[0];

        if (!rankService.rankExists(rankName)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(rankName);
        if (rank.getServers().isEmpty()) {
            player.sendMessage(CC.translate("&cThis rank has no servers."));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&l" + rank.getName() + " &7Servers:"));
        player.sendMessage(CC.translate(" &f● &b" + String.join(", ", rank.getServers())));
        player.sendMessage("");
    }
}
