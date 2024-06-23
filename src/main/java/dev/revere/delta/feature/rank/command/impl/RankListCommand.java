package dev.revere.delta.feature.rank.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class RankListCommand extends BaseCommand {
    @Command(name = "rank.list", permission = "delta.rank.list", inGameOnly = true, description = "List all ranks")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lRank List &f(" + rankService.getRanks().size() + "&f)"));
        if (rankService.getRanks().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Ranks available."));
        }

        rankService.getRanks().stream()
                .sorted((rank1, rank2) -> {
                    if (rank1.getWeight() == rank2.getWeight()) {
                        return rank1.getName().compareTo(rank2.getName());
                    }
                    return rank1.getWeight() - rank2.getWeight();
                })
                .forEach(rank -> {
                    player.sendMessage(CC.translate("      &f● &b" + rank.getName() + " &f- &7" + rank.getPrefix() + " &8(&7" + rank.getWeight() + "&8)") + (rank.isDefaultRank() ? " &8(&7Default&8)" : ""));
                });


        player.sendMessage("");
    }
}
