package dev.revere.delta.feature.grant.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.grant.GrantHandler;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.lang.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 22:05
 */
public class SetRankCommand extends BaseCommand {
    @Override
    @Command(name = "setrank", permission = "delta.command.setrank", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 4) {
            sender.sendMessage(CC.translate("&cUsage: /setrank <player> <rank> <duration> <reason>"));
            return;
        }

        String targetName = args[0];
        String rankName = args[1];
        String reason = String.join(" ", Arrays.copyOfRange(args, 4, args.length));

        Rank rank = Delta.getInstance().getServiceManager().getService(RankService.class).getRank(rankName);
        if (rank == null) {
            sender.sendMessage(CC.translate("&cThat rank does not exist."));
            return;
        }

        Player targetPlayer = sender.getServer().getPlayer(targetName);
        if (targetPlayer == null) {
            sender.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        Grant grant = new Grant();
        grant.setRank(rank.getName());
        grant.setServer(Locale.SERVER_NAME);
        grant.setReason(reason);
        grant.setAddedBy(sender.getName());
        grant.setRemovedBy(null);
        grant.setRemovedReason(null);
        grant.setAddedAt(System.currentTimeMillis());
        if (isPermanent(args[2])) {
            grant.setPermanent(true);
        } else {
            //grant.setDuration(Long.parseLong(args[2]));
        }
        grant.setActive(true);
        GrantHandler.addGrant(grant, targetPlayer.getUniqueId());
        targetPlayer.sendMessage(CC.translate("&aYou have been granted the rank " + rank.getName() + " by " + command.getSender().getName() + "."));
    }

    private boolean isPermanent(String duration) {
        return duration.equalsIgnoreCase("permanent") || duration.equalsIgnoreCase("perm");
    }
}
