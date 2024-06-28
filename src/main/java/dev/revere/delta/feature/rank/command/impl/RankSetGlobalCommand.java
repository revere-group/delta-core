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
public class RankSetGlobalCommand extends BaseCommand {
    @Command(name = "rank.setglobal", permission = "delta.rank.setglobal", inGameOnly = true, description = "Set a rank's global status")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setglobal <name> <global>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        Boolean isGlobal = getStatus(args, player);
        if (isGlobal == null) return;

        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(name);
        if (rankService.isDefaultRank(rank)) {
            player.sendMessage(CC.translate("&cYou cannot set the global status of the default rank."));
            return;
        }

        rank.setGlobal(isGlobal);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&fSuccessfully set the global status of the rank &b" + name + " &fto &b" + isGlobal + "&f."));
    }

    /**
     * Get the status from the command arguments
     *
     * @param args The command arguments
     * @param player The player executing the command
     * @return The status
     */
    private Boolean getStatus(String[] args, Player player) {
        boolean isGlobal;
        try {
            isGlobal = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            player.sendMessage(CC.translate("&cInvalid boolean value."));
            return null;
        }
        return isGlobal;
    }
}
