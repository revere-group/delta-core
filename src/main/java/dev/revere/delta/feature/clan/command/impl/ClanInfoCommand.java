package dev.revere.delta.feature.clan.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.clan.Clan;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/21/2024
 */
public class ClanInfoCommand extends BaseCommand {
    @Command(name = "clan.info", inGameOnly = true, description = "Get information about a clan")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /clan info <clan>"));
            return;
        }

        String clanName = args[0];
        Clan clan = Delta.getInstance().getClanRepository().getClan(clanName);

        if (clan == null) {
            player.sendMessage(CC.translate("&cThat clan does not exist."));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&l" + clan.getName() + " &7Information"));
        player.sendMessage(CC.translate("      &f● &bLeader: &f" + Bukkit.getOfflinePlayer(clan.getLeader()).getName()));
        player.sendMessage(CC.translate("      &f● &bMembers &7(" + clan.getMembers().size() + ")&f: " + String.join(", ", clan.getMembers().stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).toArray(String[]::new))));
        player.sendMessage(CC.translate("      &f● &bColor: &f" + clan.getColor() + clan.getColor().name()));
        player.sendMessage("");
    }
}
