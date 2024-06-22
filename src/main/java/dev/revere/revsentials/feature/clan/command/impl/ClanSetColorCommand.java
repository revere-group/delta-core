package dev.revere.revsentials.feature.clan.command.impl;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.feature.clan.Clan;
import dev.revere.revsentials.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/21/2024
 */
public class ClanSetColorCommand extends BaseCommand {
    @Command(name = "clan.setcolor", aliases = {"clan.color"}, inGameOnly = true, description = "Set the color of your clan")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /clan setcolor <color>"));
            return;
        }

        Clan clan = Revsential.getInstance().getClanRepository().getPlayerClan(player.getUniqueId());
        if (clan == null) {
            player.sendMessage(CC.translate("&cYou are not in a clan."));
            return;
        }

        String color = args[0];
        ChatColor chatColor = ChatColor.valueOf(color.toUpperCase());

        Revsential.getInstance().getClanRepository().setColor(player, chatColor);
    }
}
