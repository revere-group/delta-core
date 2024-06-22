package dev.revere.revsentials.command.player;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/20/2024
 */
public class VersionCommand extends BaseCommand {
    @Command(name = "version", aliases = {"ver", "about"}, usage = "/version")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lRevere Development &7- &fRevsentials"));
        player.sendMessage(CC.translate(" &f● &bVersion: &f" + Revsential.getInstance().getDescription().getVersion()));
        player.sendMessage(CC.translate(" &f● &bWebsite: &fwww.revere.dev"));
        player.sendMessage(CC.translate(" &f● &bAuthor: &fRemi"));
        player.sendMessage("");
    }
}
