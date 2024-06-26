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
public class RankSetDefaultCommand extends BaseCommand {
    @Command(name = "rank.setdefault", permission = "delta.rank.setdefault", inGameOnly = true, description = "Set a rank's default status")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setdefault <name> <default>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        Boolean isDefault = getStatus(args, player);
        if (isDefault == null) return;

        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank currentDefault = rankService.getDefaultRank();
        if (currentDefault != null) {
            currentDefault.setDefaultRank(false);
            rankService.saveRank(currentDefault);
        }

        Rank rank = rankService.getRank(name);
        rank.setDefaultRank(isDefault);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&fSuccessfully set the default status of the rank &b" + name + " &fto &b" + isDefault + "&f."));
    }

    /**
     * Get the status from the command arguments
     *
     * @param args The command arguments
     * @param player The player executing the command
     * @return The status
     */
    private Boolean getStatus(String[] args, Player player) {
        boolean isDefault;
        try {
            isDefault = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            player.sendMessage(CC.translate("&cInvalid boolean value."));
            return null;
        }
        return isDefault;
    }
}
