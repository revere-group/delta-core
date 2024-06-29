package dev.revere.delta.command.troll;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 11:51
 */
public class FakeExplosionCommand extends BaseCommand {
    @Override
    @Command(name = "fakeexplosion", permission = "delta.command.fakeexplosion", usage = "fakeexplosion", description = "Fake an explosion")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /fakeexplosion (player)"));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        targetPlayer.getWorld().createExplosion(targetPlayer.getLocation(), 0.0F, false, false);
        player.sendMessage(CC.translate("&bYou've fake exploded &7" + targetPlayer.getName()));
    }
}
