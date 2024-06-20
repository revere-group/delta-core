package dev.revere.revsentials.command.staff;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.profile.Profile;
import dev.revere.revsentials.service.ProfileService;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/19/2024
 */
public class StaffChatCommand extends BaseCommand {
    @Command(name = "staffchat", aliases = {"sc"}, permission = "revsentials.staff.chat", description = "Toggle staff chat", usage = "/staffchat")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Revsential.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        profile.getStaffOptions().setStaffChat(!profile.getStaffOptions().isStaffChat());
        player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("staff.chat.toggled"))
                .replace("%status%", profile.getStaffOptions().isStaffChat() ? CC.translate("&cdisabled") : CC.translate("&aenabled"))
        );
    }
}
