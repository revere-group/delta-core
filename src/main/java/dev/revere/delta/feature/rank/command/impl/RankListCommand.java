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
public class RankListCommand extends BaseCommand {
    @Command(name = "rank.list", permission = "delta.rank.list", inGameOnly = true, description = "List all ranks")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lRank List &f(" + rankService.getRanks().size() + "&f)"));
        if (rankService.getRanks().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo ranks available."));
        } else {
            rankService.getRanks().stream()
                    .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                    .forEach(rank -> {
                        BaseComponent[] hoverText = new ComponentBuilder("")
                                .append(CC.translate("&b&lRank Information:\n"))
                                .append(CC.translate(" &f● Prefix: "))
                                .append(CC.translate(rank.getPrefix())).append("\n")
                                .append(CC.translate(" &f● Suffix: "))
                                .append(CC.translate(rank.getSuffix())).append("\n")
                                .append(CC.translate(" &f● Weight: "))
                                .append(CC.translate("&b" + rank.getWeight())).append("\n")
                                .append(CC.translate(" &f● Cost: "))
                                .append(CC.translate("&b" + rank.getCost())).append("\n")
                                .append(CC.translate(" &f● Color: "))
                                .append(CC.translate(rank.getNameColor() + rank.getNameColor().name())).append("\n")
                                .append(CC.translate(" &f● Staff: "))
                                .append(CC.translate("&b" + (rank.isStaffRank() ? "Yes" : "No"))).append("\n")
                                .append(CC.translate(" &f● Default: "))
                                .append(CC.translate("&b" + (rank.isDefaultRank() ? "Yes" : "No"))).append("\n")
                                .append(CC.translate(" &f● Purchasable: "))
                                .append(CC.translate("&b" + (rank.isPurchasable() ? "Yes" : "No"))).append("\n")
                                .append(CC.translate(" &f● Global: "))
                                .append(CC.translate("&b" + (rank.isGlobal() ? "Yes" : "No"))).append("\n")
                                .append(CC.translate(" &f● Hidden: "))
                                .append(CC.translate("&b" + (rank.isHidden() ? "Yes" : "No"))).append("\n")
                                .append(CC.translate(" &f● Inheritance: "))
                                .append(CC.translate("&b" + (rank.getInheritance().isEmpty() ? "None" : String.join(", ", rank.getInheritance())))).append("\n")
                                .append(CC.translate(" &f● Permissions: "))
                                .append(CC.translate("&b" + (rank.getPermissions().isEmpty() ? "None" : String.join(", ", rank.getPermissions())))).append("\n")
                                .append(CC.translate(" &f● Servers: "))
                                .append(CC.translate("&b" + (rank.getServers().isEmpty() ? "None" : String.join(", ", rank.getServers()))))
                                .create();

                        ComponentBuilder message = new ComponentBuilder("      ");
                        message.append("● ").color(ChatColor.WHITE.asBungee());
                        message.append(rank.getName()).color(ChatColor.AQUA.asBungee());
                        message.append(" - ").color(ChatColor.WHITE.asBungee());
                        message.append(CC.translate(rank.getPrefix()));
                        message.append(" ").color(ChatColor.DARK_GRAY.asBungee());
                        message.append("(" + rank.getWeight() + ")").color(ChatColor.DARK_GRAY.asBungee());

                        if (rank.isDefaultRank()) {
                            message.append(" (Default)").color(ChatColor.DARK_GRAY.asBungee());
                        }

                        message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));

                        player.spigot().sendMessage(message.create());
                    });
        }

        player.sendMessage("");
    }
}
