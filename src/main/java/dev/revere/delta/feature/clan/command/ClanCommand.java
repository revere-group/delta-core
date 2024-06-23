package dev.revere.delta.feature.clan.command;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class ClanCommand extends BaseCommand {
    @Command(name = "clan", inGameOnly = true, description = "Main clan command")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lClan Commands Help:"));
        player.sendMessage(CC.translate(" &f● &b/clan setcolor &8(&7color&8) &7| Set the color of your clan"));
        player.sendMessage(CC.translate(" &f● &b/clan invite &8(&7player&8) &7| Invite a player to your clan"));
        player.sendMessage(CC.translate(" &f● &b/clan kick &8(&7player&8) &7| Kick a player from your clan"));
        player.sendMessage(CC.translate(" &f● &b/clan info &8(&7clan&8) &7| View information about a clan"));
        player.sendMessage(CC.translate(" &f● &b/clan home &7| Teleport to the home of your clan"));
        player.sendMessage(CC.translate(" &f● &b/clan sethome &7| Set the home of your clan"));
        player.sendMessage(CC.translate(" &f● &b/clan create &8(&7clan&8) &7| Create a clan"));
        player.sendMessage(CC.translate(" &f● &b/clan delete &8(&7clan&8) &7| Delete a clan"));
        player.sendMessage(CC.translate(" &f● &b/clan chat &7| Toggle clan chat"));
        player.sendMessage(CC.translate(" &f● &b/clan list &7| List all clans"));
        player.sendMessage("");
    }
}
