package dev.revere.delta.command.troll;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class CaptureCommand extends BaseCommand {
    @Command(name = "capture", permission = "delta.troll.capture", description = "Capture a player", usage = "/capture <player>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /capture <player>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        target.teleport(player.getLocation());

        Location location = player.getLocation();
        World world = location.getWorld();

        int px = location.getBlockX();
        int py = location.getBlockY();
        int pz = location.getBlockZ();

        for (int x = px - 2; x <= px + 2; x++) {
            for (int y = py - 2; y <= py + 3; y++) {
                for (int z = pz - 2; z <= pz + 2; z++) {
                    if (x == px - 2 || x == px + 2 || y == py - 2 || y == py + 3 || z == pz - 2 || z == pz + 2) {
                        assert world != null;
                        world.getBlockAt(x, y, z).setType(Material.GLASS);
                    }
                }
            }
        }

        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("trolling.capture.message"))
                .replace("%target%", target.getName()));
    }
}
