package dev.revere.delta.command.troll;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 20:26
 */
public class PushCommand extends BaseCommand {
    @Override
    @Command(name = "push", permission = "delta.command.push", usage = "push (player) (value)", description = "Push a player")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /push (player) (value)"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        double value;

        try {
            value = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        target.setVelocity(player.getLocation().getDirection().multiply(value));

        player.sendMessage(CC.translate("&fYou've pushed &b" + target.getName()));
        target.sendMessage(CC.translate("&fYou've been pushed by &b" + player.getName()));


    }
}
