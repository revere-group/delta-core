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
 * @date 6/17/2024
 */
public class RankListPermissionsCommand extends BaseCommand {
    @Command(name = "rank.listpermissions", aliases = {"rank.listperms"}, permission = "delta.rank.listpermissions", inGameOnly = true, description = "List rank permissions")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /rank listpermissions <rank>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);
        String rankName = args[0];

        if (!rankService.rankExists(rankName)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        Rank rank = rankService.getRank(rankName);
        if (rank.getPermissions().isEmpty()) {
            player.sendMessage(CC.translate("&cThis rank has no permissions."));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&l" + rank.getName() + " &7Permissions:"));
        player.sendMessage(CC.translate(" &f● &b" + String.join(", ", rank.getPermissions())));
        player.sendMessage("");
    }
}
