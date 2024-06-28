package dev.revere.delta.command.admin;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/28/2024
 */
public class AllPlayersCommand extends BaseCommand {
    @Command(name = "allplayers", permission = "delta.command.allplayers")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lAll Players &7Information:"));
        player.sendMessage(CC.translate(" &f● &fTotal: &b" + Delta.getInstance().getServiceManager().getService(ProfileService.class).getCollection().countDocuments()));
        player.sendMessage(CC.translate(""));
    }
}
