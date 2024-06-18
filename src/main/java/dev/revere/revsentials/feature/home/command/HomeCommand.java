package dev.revere.revsentials.feature.home.command;

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
public class HomeCommand extends BaseCommand {
    @Command(name = "home", inGameOnly = true, description = "Main home command")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lHome Commands Help:"));
        player.sendMessage(CC.translate(" &f● &b/home create &8(&7homeName&8) &7| Create a home"));
        player.sendMessage(CC.translate(" &f● &b/home delete &8(&7homeName&8) &7| Delete a home"));
        player.sendMessage(CC.translate(" &f● &b/home go &8(&7homeName&8) &7| Go to a home"));
        player.sendMessage(CC.translate(" &f● &b/home menu &7| Open the home menu"));
        player.sendMessage(CC.translate(" &f● &b/home list &7| List all homes"));
        player.sendMessage("");
    }
}
