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
 * @author Emmy
 * @project Delta
 * @date 20/06/2024 - 05:46
 */
public class BroadcastCommand extends BaseCommand {
    @Override
    @Command(name = "broadcast", description = "Broadcast a message to the server", permission = "delta.command.broadcast")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /broadcast (message)"));
            return;
         }

        List<String> message = Arrays.asList(args);
        String alertMessage = String.join(" ", message);

        for (String stringList : Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getStringList("broadcasts.broadcast")) {
            player.getServer().getOnlinePlayers().forEach(online -> online.sendMessage(CC.translate(stringList)
                    .replace("%message%", alertMessage))
            );
        }
    }
}
