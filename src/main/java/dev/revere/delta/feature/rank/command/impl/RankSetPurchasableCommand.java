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
public class RankSetPurchasableCommand extends BaseCommand {
    @Command(name = "rank.setpurchasable", permission = "delta.rank.setpurchasable", inGameOnly = true, description = "Set a rank's purchasable status")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setpurchasable <name> <purchasable>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        Boolean isPurchasable = getStatus(args, player);
        if (isPurchasable == null) return;

        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(name);
        if (rankService.isDefaultRank(rank)) {
            player.sendMessage(CC.translate("&cYou cannot change the purchasable status of the default rank."));
            return;
        }

        rank.setPurchasable(isPurchasable);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&fYou have set &b" + rank.getName() + "'s &fpurchasable status to &b" + isPurchasable + "&f."));
    }

    /**
     * Get the status from the command arguments
     *
     * @param args The command arguments
     * @param player The player executing the command
     * @return The status
     */
    private Boolean getStatus(String[] args, Player player) {
        boolean isPurchasable;
        try {
            isPurchasable = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            player.sendMessage(CC.translate("&cInvalid boolean value."));
            return null;
        }
        return isPurchasable;
    }
}
