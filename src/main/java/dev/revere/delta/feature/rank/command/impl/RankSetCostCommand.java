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
 * @date 6/23/2024
 */
public class RankSetCostCommand extends BaseCommand {
    @Command(name = "rank.setcost", permission = "delta.rank.setcost", inGameOnly = true, description = "Set a rank's cost")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setcost <name> <cost>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        Integer cost = getCost(args, player);
        if (cost == null) return;

        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(name);
        rank.setCost(cost);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&fSuccessfully set the cost of the rank &b" + name + " &fto &b" + cost + "&f."));
    }


    /**
     * Get the cost from the command arguments
     *
     * @param args   The command arguments
     * @param player The player executing the command
     * @return The cost
     */
    private Integer getCost(String[] args, Player player) {
        int cost;
        try {
            cost = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cThe cost must be a number."));
            return null;
        }
        return cost;
    }
}
