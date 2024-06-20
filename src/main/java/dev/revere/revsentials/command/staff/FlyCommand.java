package dev.revere.revsentials.command.staff;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Revsentials
 * Date: 20/06/2024 - 16:38
 */
public class FlyCommand extends BaseCommand {
    @Override
    @Command(name = "fly", aliases = {"flight"}, permission = "revsentials.fly")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        String enabled = CC.translate("&aenabled");
        String disabled = CC.translate("&cdisabled");

        if (args.length < 1) {
            player.setAllowFlight(!player.getAllowFlight());
            player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("fly-command.message"))
                    .replace("%status%", player.getAllowFlight() ? enabled : disabled)
            );
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("fly-command.player-not-found")));
            return;
        }

        target.setAllowFlight(!target.getAllowFlight());
        target.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("fly-command.changed-by"))
                .replace("%status%", target.getAllowFlight() ? enabled : disabled)
                .replace("%player%", player.getName())
        );

        player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("fly-command.message-target"))
                .replace("%target%", target.getName())
                .replace("%status%", target.getAllowFlight() ? enabled : disabled)
        );
    }
}
