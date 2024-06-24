package dev.revere.delta.feature.conversation.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.conversation.ConversationService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Remi
 * @project Delta
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

        Profile targetProfile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(target.getUniqueId());
        if (targetProfile.getStaffOptions().isVanish()) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        if (player.equals(target)) {
            player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("conversation.cant-msg-self")));
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Delta.getInstance().getServiceManager().getService(ConversationService.class).sendMessages(player.getUniqueId(), target.getUniqueId(), message);
    }
}
