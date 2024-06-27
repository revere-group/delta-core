package dev.revere.delta.command.staff.teleport;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 27/06/2024 - 19:40
 */
public class TeleportPositionCommand extends BaseCommand {
    @Override
    @Command(name = "teleportposition", permission = "delta.staff.tplocation", aliases = {"tploc", "tp-pos", "tppos"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 3) {
            player.sendMessage(CC.translate("&cUsage: /tppos (x) (y) (z)"));
            return;
        }

        double x, y, z;
        try {
            x = Double.parseDouble(args[0]);
            y = Double.parseDouble(args[1]);
            z = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number format."));
            return;
        }

        if (x > 30000000 || x < -30000000 || y > 30000000 || y < -30000000 || z > 30000000 || z < -30000000) {
            player.sendMessage(CC.translate("&cYou can't teleport further than 30000000/-30000000."));
            return;
        }

        player.teleport(player.getWorld().getBlockAt((int) x, (int) y, (int) z).getLocation());
        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.teleport.tp-pos")
                .replace("%x%", String.valueOf(x))
                .replace("%y%", String.valueOf(y))
                .replace("%z%", String.valueOf(z)))
        );
    }
}
