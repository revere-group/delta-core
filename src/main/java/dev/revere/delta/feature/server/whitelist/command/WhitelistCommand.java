package dev.revere.delta.feature.server.whitelist.command;

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
public class WhitelistCommand extends BaseCommand {
    @Command(name = "whitelist", inGameOnly = true, description = "Main rank command")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lWhitelist Commands Help:"));
        player.sendMessage(CC.translate(" &f● &b/whitelist toggle &7| Toggle the whitelist"));
        player.sendMessage(CC.translate(" &f● &b/whitelist status &7| Check the status of the whitelist"));
        player.sendMessage("");
    }
}
