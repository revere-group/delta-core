package dev.revere.delta.feature.server.whitelist.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.server.whitelist.WhitelistService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
public class WhitelistStatusCommand extends BaseCommand {
    @Command(name = "whitelist.status", inGameOnly = true, description = "Check the status of the whitelist")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        WhitelistService whitelistService = Delta.getInstance().getServiceManager().getService(WhitelistService.class);

        player.sendMessage("");
        player.sendMessage(CC.translate("&fWhitelist is currently " + (whitelistService.isWhitelistEnabled() ? "&aenabled" : "&cdisabled")));
        player.sendMessage("");
    }
}
