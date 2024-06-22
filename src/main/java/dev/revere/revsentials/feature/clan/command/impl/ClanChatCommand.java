package dev.revere.revsentials.feature.clan.command.impl;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.feature.clan.Clan;
import dev.revere.revsentials.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/21/2024
 */
public class ClanChatCommand extends BaseCommand {
    @Command(name = "clan.chat", aliases = {"cc"}, inGameOnly = true, description = "Talk in clan chat")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /clan chat <message>"));
            return;
        }

        Clan clan = Revsential.getInstance().getClanRepository().getPlayerClan(player.getUniqueId());
        if (clan == null) {
            player.sendMessage(CC.translate("&cYou are not in a clan."));
            return;
        }

        String message = String.join(" ", args);
        Revsential.getInstance().getClanRepository().broadcastClanMessage(clan, CC.translate("&f[" + clan.getColor() + "CLAN CHAT&f] " + clan.getColor() + player.getName() + ": &f" + message));
    }
}
