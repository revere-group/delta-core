package dev.revere.delta.feature.grant.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.grant.GrantService;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.feature.server.ServerService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.DateUtils;
import dev.revere.delta.util.lang.Locale;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Objects;

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
        String duration = args[2];
        String reason = String.join(" ", Arrays.copyOfRange(args, 3, args.length));

        Rank rank = Delta.getInstance().getServiceManager().getService(RankService.class).getRank(rankName);
        if (rank == null) {
            sender.sendMessage(CC.translate("&cThat rank does not exist."));
            return;
        }

        OfflinePlayer targetPlayer = sender.getServer().getOfflinePlayer(targetName);
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(targetPlayer.getUniqueId());

        GrantService grantService = Delta.getInstance().getServiceManager().getService(GrantService.class);
        Grant existingGrant = grantService.getActiveGrant(profile, rankName);
        if (existingGrant != null) {
            sender.sendMessage(CC.translate("&cPlayer already has this rank."));
            return;
        }

        ServerService serverService = Delta.getInstance().getServiceManager().getService(ServerService.class);

        Grant grant = new Grant();
        grant.setRankName(rank.getName());
        grant.setServer(serverService.getServerName());
        grant.setReason(reason);
        grant.setAddedBy(sender.getName());
        grant.setRemovedBy(null);
        grant.setRemovedReason(null);
        grant.setAddedAt(System.currentTimeMillis());
        if (isPermanent(args[2])) {
            grant.setPermanent(true);
        } else {
            grant.setDuration(DateUtils.parseTime(duration));
        }
        grant.setActive(true);
        grantService.addGrant(grant, targetPlayer.getUniqueId());
        Objects.requireNonNull(targetPlayer.getPlayer()).sendMessage(CC.translate("&aYou have been granted the rank " + rank.getName() + " by " + command.getSender().getName() + "."));
    }

    private boolean isPermanent(String duration) {
        return duration.equalsIgnoreCase("permanent") || duration.equalsIgnoreCase("perm");
    }
}
