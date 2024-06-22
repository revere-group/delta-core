package dev.revere.revsentials.command.player;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class RevsentialsCommand extends BaseCommand {
    @Command(name = "revsentials", description = "Main command for Revsentials")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(" ");
            player.sendMessage(CC.translate("  &b&l   Revsentials"));
            player.sendMessage(CC.translate("      &f┃ Author: &b" + Revsential.getInstance().getDescription().getAuthors()).replace("[", "").replace("]", ""));
            player.sendMessage(CC.translate("      &f┃ Version: &b" + Revsential.getInstance().getDescription().getVersion()));
            player.sendMessage(CC.translate(" "));
            player.sendMessage(CC.translate("  &b&l   Description:"));
            player.sendMessage(CC.translate("      &f┃ " + Revsential.getInstance().getDescription().getDescription()));
            player.sendMessage(" ");
        }
    }
}
