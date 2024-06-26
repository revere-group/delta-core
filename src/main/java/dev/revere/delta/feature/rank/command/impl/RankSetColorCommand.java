package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class RankSetColorCommand extends BaseCommand {
    @Command(name = "rank.setcolor", permission = "delta.rank.setcolor", inGameOnly = true, description = "Set a rank's color")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /rank setcolor <name> <color>"));
            return;
        }

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        String name = args[0];
        String color = args[1];

        if (!rankService.rankExists(name)) {
            player.sendMessage(CC.translate("&cA rank with that name does not exist."));
            return;
        }

        if (Arrays.stream(ChatColor.values()).noneMatch(chatColor -> chatColor.name().equalsIgnoreCase(color))) {
            player.sendMessage(CC.translate("&cThat is not a valid color."));
            return;
        }

        Rank rank = rankService.getRank(name);
        ChatColor chatColor = ChatColor.valueOf(color.toUpperCase());
        rank.setNameColor(chatColor);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&fSuccessfully set the color of the rank &b" + name + " &fto " + chatColor + color + "&f."));
    }
}
