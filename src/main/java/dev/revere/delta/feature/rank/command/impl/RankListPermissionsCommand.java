package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.CC;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Comparator;

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
        rank.getPermissions().forEach(permission -> player.sendMessage(CC.translate(" &f● &7" + permission)));
        player.sendMessage("");
    }
}
