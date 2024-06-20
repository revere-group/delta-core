package dev.revere.revsentials.command.player.conversation;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.service.ConversationService;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/19/2024
 */
public class MessageCommand extends BaseCommand {
    @Command(name = "message", aliases = {"msg", "tell", "whisper"}, usage = "/message <player> <message>", description = "Send a private message to a player")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /message <player> <message>"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        if (player.equals(target)) {
            player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("conversation.cant-msg-self")));
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Revsential.getInstance().getServiceManager().getService(ConversationService.class).sendMessages(player.getUniqueId(), target.getUniqueId(), message);
    }
}
