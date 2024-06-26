package dev.revere.delta.feature.grant.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.grant.GrantService;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 22:05
 */
public class RemoveRankCommand extends BaseCommand {
    @Override
    @Command(name = "removerank", permission = "delta.command.removerank", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&cUsage: /removerank <player> <rank> <reason>"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer targetPlayer = sender.getServer().getOfflinePlayer(targetName);

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(targetPlayer.getUniqueId());

        String rankName = args[1];
        Rank rank = Delta.getInstance().getServiceManager().getService(RankService.class).getRank(rankName);
        if (rank == null) {
            sender.sendMessage(CC.translate("&cThat rank does not exist."));
            return;
        }

        String reason = args.length > 2 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : "No reason provided.";

        GrantService grantService = Delta.getInstance().getServiceManager().getService(GrantService.class);
        Grant grant = grantService.getActiveGrant(profile, rankName);

        if (grant == null) {
            sender.sendMessage(CC.translate("&cPlayer does not have this rank."));
            return;
        }

        grantService.removeGrant(grant.getRankName(), reason, targetPlayer.getUniqueId(), sender);
        sender.sendMessage(CC.translate("&aSuccessfully removed rank from player."));
    }
}
