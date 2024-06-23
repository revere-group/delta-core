package dev.revere.delta.command.admin;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class AlertCommand extends BaseCommand {
    @Command(name = "alert", permission = "delta.admin.alert", description = "Alerts all players on the server", inGameOnly = false, usage = "/alert <message>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /alert <message>"));
            return;
        }

        List<String> message = Arrays.asList(args);
        String alertMessage = String.join(" ", message);

        for (String stringList : Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getStringList("broadcasts.alert")) {
            player.getServer().getOnlinePlayers().forEach(online -> online.sendMessage(CC.translate(stringList)
                    .replace("%message%", CC.translate(alertMessage)))
            );
        }
    }
}
