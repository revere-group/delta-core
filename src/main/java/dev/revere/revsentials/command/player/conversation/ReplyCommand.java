package dev.revere.revsentials.command.player.conversation;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.service.ConversationService;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/19/2024
 */
public class ReplyCommand extends BaseCommand {
    @Command(name = "reply", aliases = {"r"}, usage = "/reply <message>", description = "Reply to the last person who messaged you")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /reply <message>"));
            return;
        }

        UUID lastConversation = Revsential.getInstance().getServiceManager().getService(ConversationService.class).getConversation(player.getUniqueId());
        if (lastConversation == null) {
            player.sendMessage(CC.translate("&cYou have no one to reply to."));
            return;
        }

        Player target = player.getServer().getPlayer(lastConversation);
        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        String message = String.join(" ", args);
        Revsential.getInstance().getServiceManager().getService(ConversationService.class).sendMessages(player.getUniqueId(), target.getUniqueId(), message);
    }
}
