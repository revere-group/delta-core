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
public class RankRemovePermissionCommand extends BaseCommand {
    @Command(name = "rank.removepermission", aliases = {"rank.removeperm"}, permission = "delta.rank.removepermission", inGameOnly = true, description = "Remove a permission from a rank")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank removepermission <name> <permission>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        String permission = args[1];

        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(name);

        List<String> permissions = rank.getPermissions();
        if (!permissions.contains(permission)) {
            player.sendMessage(CC.translate("&cThe rank &b" + name + " &cdoes not have the permission &b" + permission + "&c."));
            return;
        }

        permissions.remove(permission);
        rank.setPermissions(permissions);
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

        player.sendMessage(CC.translate("&fSuccessfully removed the permission &b" + permission + " &ffrom the rank &b" + name + "&f."));
    }
}
