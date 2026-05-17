package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.CC;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

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

        String translatedColor = CC.translateColor(color);
        if (translatedColor == null) {
            player.sendMessage(CC.translate("&cInvalid color input. Please use a valid color name or hex format (e.g., <#RRGGBB>)."));
            return;
        }

        ChatColor chatColor = ChatColor.of(translatedColor);
        if (chatColor == null) {
            player.sendMessage(CC.translate("&cInvalid color input. Please use a valid color name or hex format (e.g., <#RRGGBB>)."));
            return;
        }

        Rank rank = rankService.getRank(name);
        rank.setNameColor(chatColor);
        rankService.saveRank(rank);

        player.sendMessage(CC.translate("&fSuccessfully set the color of the rank &b" + name + " &fto " + chatColor + translatedColor + "&f."));
    }
}
