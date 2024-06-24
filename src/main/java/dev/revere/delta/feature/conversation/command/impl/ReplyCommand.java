package dev.revere.delta.feature.conversation.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.conversation.ConversationService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Remi
 * @project Delta
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

        UUID lastConversation = Delta.getInstance().getServiceManager().getService(ConversationService.class).getConversation(player.getUniqueId());
        if (lastConversation == null) {
            player.sendMessage(CC.translate("&cYou have no one to reply to."));
            return;
        }

        Player target = player.getServer().getPlayer(lastConversation);
        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        Profile targetProfile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(target.getUniqueId());
        if (targetProfile.getStaffOptions().isVanish()) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        String message = String.join(" ", args);
        Delta.getInstance().getServiceManager().getService(ConversationService.class).sendMessages(player.getUniqueId(), target.getUniqueId(), message);
    }
}
