package dev.revere.revsentials.command.player;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class PingCommand extends BaseCommand {
    @Command(name = "ping", usage = "/ping [player]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("player.ping"))
                    .replace("%ping%", String.valueOf(player.getPing()))
            );
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("player.ping-target"))
                .replace("%target%", target.getName())
                .replace("%ping%", String.valueOf(target.getPing()))
        );
    }
}
