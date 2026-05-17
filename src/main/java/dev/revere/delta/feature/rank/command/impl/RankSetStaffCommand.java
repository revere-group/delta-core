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
public class RankSetStaffCommand extends BaseCommand {
    @Command(name = "rank.setstaff", permission = "delta.rank.setstaff", inGameOnly = true, description = "Set a rank's staff status")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setstaff <name> <staff>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        Boolean isStaff = getStatus(args, player);
        if (isStaff == null) return;

        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(name);
        rank.setStaffRank(isStaff);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&fSuccessfully set the staff status of the rank &b" + name + " &fto &b" + isStaff + "&f."));
    }

    /**
     * Get the status from the command arguments
     *
     * @param args   The command arguments
     * @param player The player executing the command
     * @return The status
     */
    private Boolean getStatus(String[] args, Player player) {
        boolean isStaff;
        try {
            isStaff = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            player.sendMessage(CC.translate("&cInvalid boolean value."));
            return null;
        }
        return isStaff;
    }
}
