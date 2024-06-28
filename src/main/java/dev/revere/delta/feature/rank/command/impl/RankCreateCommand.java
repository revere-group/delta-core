package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class RankCreateCommand extends BaseCommand {
    @Command(name = "rank.create", permission = "delta.rank.create", inGameOnly = true, description = "Create a rank")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /rank create <name>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        if (rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name already exists."));
            return;
        }

        List<String> permissions = Collections.emptyList();
        List<String> inheritance = Collections.emptyList();
        List<String> servers = Collections.emptyList();
        Rank rank = new Rank(name, "&7" + name, "&7", 0, 0, false, false, false, true, false, inheritance, permissions, servers, ChatColor.GRAY);
        rankService.createRank(rank);

        player.sendMessage(CC.translate("&fSuccessfully created the rank &b" + name + "&f."));
    }
}
