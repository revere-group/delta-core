package dev.revere.delta.command.staff;

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
 * @date 20/06/2024 - 16:38
 */
public class FlyCommand extends BaseCommand {
    @Override
    @Command(name = "fly", aliases = {"flight"}, permission = "delta.fly")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        String enabled = CC.translate("&aenabled");
        String disabled = CC.translate("&cdisabled");

        if (args.length < 1) {
            player.setAllowFlight(!player.getAllowFlight());
            player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("fly-command.message"))
                    .replace("%status%", player.getAllowFlight() ? enabled : disabled)
            );
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("fly-command.player-not-found")));
            return;
        }

        target.setAllowFlight(!target.getAllowFlight());
        target.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("fly-command.changed-by"))
                .replace("%status%", target.getAllowFlight() ? enabled : disabled)
                .replace("%player%", player.getName())
        );

        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("fly-command.message-target"))
                .replace("%target%", target.getName())
                .replace("%status%", target.getAllowFlight() ? enabled : disabled)
        );
    }
}
