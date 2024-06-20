package dev.revere.revsentials.command.admin;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Emmy
 * Project: Revsentials
 * Date: 20/06/2024 - 05:46
 */
public class BroadcastCommand extends BaseCommand {
    @Override
    @Command(name = "broadcast", description = "Broadcast a message to the server", permission = "revsentials.command.broadcast")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /broadcast (message)"));
            return;
         }

        List<String> message = Arrays.asList(args);
        String alertMessage = String.join(" ", message);

        for (String stringList : Revsential.getInstance().getConfig("messages.yml").getStringList("broadcasts.broadcast")) {
            player.getServer().getOnlinePlayers().forEach(online -> online.sendMessage(CC.translate(stringList)
                    .replace("%message%", alertMessage))
            );
        }
    }
}
