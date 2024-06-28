package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.feature.server.ServerService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class RankAddServerCommand extends BaseCommand {
    @Command(name = "rank.addserver", permission = "delta.rank.addserver", inGameOnly = true, description = "Add a server to a rank")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank addserver <name> <server>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        String server = args[1];

        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        ServerService serverService = Delta.getInstance().getServiceManager().getService(ServerService.class);
        if (serverService.getServer(server) == null) {
            player.sendMessage(CC.translate("&cA server with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(name);
        List<String> servers = rank.getServers();
        if (servers.contains(server)) {
            player.sendMessage(CC.translate("&cThe rank &b" + name + " &calready has the server &b" + server + " &cadded."));
            return;
        }

        servers.add(server);
        rank.setServers(servers);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&b" + server + " &fhas been added to the rank &b" + name + "&f."));
    }
}
