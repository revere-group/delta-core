package dev.revere.delta.feature.server.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.server.Server;
import dev.revere.delta.feature.server.ServerService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/28/2024
 */
public class SpawnCommand extends BaseCommand {
    @Command(name = "spawn")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ServerService serverService = Delta.getInstance().getServiceManager().getService(ServerService.class);
        Server server = serverService.getServer(serverService.getServerName());

        if (server.getSpawnLocation() == null) {
            player.sendMessage(CC.translate("&cThe spawn location for this server has not been set."));
            return;
        }

        player.teleport(server.getSpawnLocation());
        player.sendMessage(CC.translate("&aYou have been teleported to the spawn location."));
    }

    @Command(name = "setspawn", permission = "delta.staff.spawn.set")
    public void onSetSpawn(CommandArgs command) {
        Player player = command.getPlayer();

        ServerService serverService = Delta.getInstance().getServiceManager().getService(ServerService.class);
        Server server = serverService.getServer(serverService.getServerName());
        serverService.saveSpawnpoint(server.getName(), player.getLocation());

        player.sendMessage(CC.translate("&aYou have set the spawn location for this server."));
    }
}
