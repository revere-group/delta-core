package dev.revere.delta.command.player;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/20/2024
 */
public class VersionCommand extends BaseCommand {
    @Command(name = "version", aliases = {"ver", "about"}, usage = "/version")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(" ");
        player.sendMessage(CC.translate("  &b&l   Delta"));
        player.sendMessage(CC.translate("      &f┃ Author: &b" + Delta.getInstance().getDescription().getAuthors()).replace("[", "").replace("]", ""));
        player.sendMessage(CC.translate("      &f┃ Version: &b" + Delta.getInstance().getDescription().getVersion()));
        player.sendMessage(CC.translate(" "));
        player.sendMessage(CC.translate("  &b&l   Description:"));
        player.sendMessage(CC.translate("      &f┃ " + Delta.getInstance().getDescription().getDescription()));
        player.sendMessage(" ");
    }
}
