package dev.revere.delta.feature.rank.command;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class RankCommand extends BaseCommand {
    @Command(name = "rank", inGameOnly = true, description = "Main rank command")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lRank Commands Help:"));
        player.sendMessage(CC.translate(" &f● &b/rank create &8(&7name&8) &7| Create a rank"));
        player.sendMessage(CC.translate(" &f● &b/rank delete &8(&7name&8) &7| Delete a rank"));
        player.sendMessage(CC.translate(" &f● &b/rank setprefix &8(&7name&8) &8(&7prefix&8) &7| Set a rank's prefix"));
        player.sendMessage(CC.translate(" &f● &b/rank setsuffix &8(&7name&8) &8(&7suffix&8) &7| Set a rank's suffix"));
        player.sendMessage(CC.translate(" &f● &b/rank setweight &8(&7name&8) &8(&7weight&8) &7| Set a rank's weight"));
        player.sendMessage(CC.translate(" &f● &b/rank setdefault &8(&7name&8) &7| Set a rank as the default rank"));
        player.sendMessage(CC.translate(" &f● &b/rank addpermission &8(&7name&8) &8(&7permission&8) &7| Add a permission to a rank"));
        player.sendMessage(CC.translate(" &f● &b/rank removepermission &8(&7name&8) &8(&7permission&8) &7| Remove a permission from a rank"));
        player.sendMessage(CC.translate(" &f● &b/rank listpermissions &8(&7name&8) &7| List all permissions of a rank"));
        player.sendMessage(CC.translate(" &f● &b/rank view &8(&7name&8) &7| View a rank's information"));
        player.sendMessage(CC.translate(" &f● &b/rank menu &7| Open the rank menu"));
        player.sendMessage(CC.translate(" &f● &b/rank list &7| List all ranks"));
        player.sendMessage("");
    }
}
