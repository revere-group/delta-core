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
public class RankSetWeightCommand extends BaseCommand {
    @Command(name = "rank.setweight", permission = "delta.rank.setweight", inGameOnly = true, description = "Set a rank's weight")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setweight <name> <weight>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        Integer weight = getWeight(args, player);
        if (weight == null) return;

        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(name);
        rank.setWeight(weight);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&fSuccessfully set the weight of the rank &b" + name + " &fto &b" + weight + "&f."));
    }


    /**
     * Get the weight from the command arguments
     *
     * @param args   The command arguments
     * @param player The player executing the command
     * @return The weight
     */
    private Integer getWeight(String[] args, Player player) {
        int weight;
        try {
            weight = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cThe weight must be a number."));
            return null;
        }
        return weight;
    }
}
