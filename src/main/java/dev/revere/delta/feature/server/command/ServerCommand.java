package dev.revere.delta.feature.server.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.server.ServerService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/27/2024
 */
public class ServerCommand extends BaseCommand {
    @Command(name = "server", permission = "delta.staff.server")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ServerService serverService = Delta.getInstance().getServiceManager().getService(ServerService.class);

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lServer &7Information"));
        player.sendMessage(CC.translate(" &f● &fCurrent Server: &b" + serverService.getServerName()));
        player.sendMessage(CC.translate(" &f● &fServers: &b" + String.join(", ", serverService.getServers())));
    }
}
