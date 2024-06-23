package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class RankAddPermissionCommand extends BaseCommand {
    @Command(name = "rank.addpermission", aliases = {"rank.addperm"}, permission = "delta.rank.addpermission", inGameOnly = true, description = "Add a permission to a rank")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank addpermission <name> <permission>"));
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
        if (permissions.contains(permission)) {
            player.sendMessage(CC.translate("&cThe rank &b" + name + " &calready has the permission &b" + permission + "&c."));
            return;
        }

        permissions.add(permission);
        rank.setPermissions(permissions);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&fSuccessfully added the permission &b" + permission + " &fto the rank &b" + name + "&f."));
    }
}
