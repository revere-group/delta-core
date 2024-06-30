package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.grant.GrantService;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class RankDeleteCommand extends BaseCommand {
    @Command(name = "rank.delete", permission = "delta.rank.delete", inGameOnly = true, description = "Delete a rank")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /rank delete <name>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        if (rankService.isDefaultRank(rankService.getRank(name))) {
            player.sendMessage(CC.translate("&cYou cannot delete the default rank."));
            return;
        }

        Rank rank = rankService.getRank(name);
        rankService.deleteRank(rank);

        GrantService grantService = Delta.getInstance().getServiceManager().getService(GrantService.class);
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        profile.getGrants().stream().filter(grant -> grant.getRank().equals(rank)).forEach(grant -> {
            grantService.deleteGrant(grant, profile);
        });

        player.sendMessage(CC.translate("&fSuccessfully deleted the rank &b" + name + "&f."));
    }
}
