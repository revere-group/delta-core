package dev.revere.delta.command.staff;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/19/2024
 */
public class StaffChatCommand extends BaseCommand {
    @Command(name = "staffchat", aliases = {"sc"}, permission = "delta.staff.chat", description = "Toggle staff chat", usage = "/staffchat")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        profile.getStaffOptions().setStaffChat(!profile.getStaffOptions().isStaffChat());
        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.chat.toggled"))
                .replace("%status%", profile.getStaffOptions().isStaffChat() ? CC.translate("&aenabled") : CC.translate("&cdisabled"))
        );
    }
}
