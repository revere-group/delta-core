package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class RankRemoveInheritanceCommand extends BaseCommand {
    @Command(name = "rank.removeinheritance", aliases = {"rank.removeinher"}, permission = "delta.rank.removeinheritance", inGameOnly = true, description = "Add a permission to a rank")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank removeinheritance <rank> <inheritrank>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String rankName = args[0];
        String inheritRankName = args[1];

        Rank rank = rankService.getRank(rankName);
        Rank inheritRank = rankService.getRank(inheritRankName);

        if (rank == null) {
            player.sendMessage(CC.translate("&cThe rank &b" + rankName + " &cdoes not exist."));
            return;
        }

        if (inheritRank == null) {
            player.sendMessage(CC.translate("&cThe rank &b" + inheritRankName + " &cdoes not exist."));
            return;
        }

        List<String> inheritances = rank.getInheritance();
        if (!inheritances.contains(inheritRankName)) {
            player.sendMessage(CC.translate("&cThe rank &b" + rankName + " &cdoes not inherit from &b" + inheritRankName + "&c."));
            return;
        }

        inheritances.remove(inheritRankName);
        rank.setInheritance(inheritances);
        rankService.saveRank(rank);

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        profileService.getProfiles().values().stream()
                .filter(profile -> profile.getGrants().equals(rank))
                .forEach(profile -> {
                    Player target = Delta.getInstance().getServer().getPlayer(profile.getUuid());
                    if (target != null) {
                        profileService.loadPermissions(target);
                    }
                });

        player.sendMessage(CC.translate("&b" + rankName + " &7no longer inherits from &b" + inheritRankName + "&7."));
    }
}
