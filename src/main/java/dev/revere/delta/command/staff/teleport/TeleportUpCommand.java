package dev.revere.delta.command.staff.teleport;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.LocationUtils;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 27/06/2024 - 19:44
 */
public class TeleportUpCommand extends BaseCommand {
    @Override
    @Command(name = "teleportup", permission = "delta.staff.tpup", aliases = {"tpup", "tp-up"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.teleport(LocationUtils.teleportUp(player.getLocation()));
        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.teleport.tp-up")));
    }
}
