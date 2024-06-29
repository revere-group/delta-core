package dev.revere.delta.command.staff;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 07:50
 */
public class HealthCommand extends BaseCommand {
    @Override
    @Command(name = "health", permission = "delta.staff.health", aliases = {"hp"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /health (player)"));
            return;
        }

        Player targetPlayer = player.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&fNo player matching &b" + args[0] + " &fis connected to this server."));
            return;
        }

        player.sendMessage(CC.translate("&f" + targetPlayer.getName() + "'s health: &b" + targetPlayer.getHealth() + "/10" + " " + "❤"));
    }
}
