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

    private final String[][] pages = {
            {
                    "&b&lRank Commands Help (Page 1):",
                    " &f● &b/rank view &8(&7name&8) &7| View a rank's information",
                    " &f● &b/rank create &8(&7name&8) &7| Create a rank",
                    " &f● &b/rank delete &8(&7name&8) &7| Delete a rank",
                    " &f● &b/rank menu &7| Open the rank menu",
                    " &f● &b/rank list &7| List all ranks",
            },
            {
                    "&b&lRank Commands Help (Page 2):",
                    " &f● &b/rank setpurchaseable &8(&7name&8) &8(&7value&8) &7| Set a rank as purchasable",
                    " &f● &b/rank setdefault &8(&7name&8) &8(&7value&8) &7| Set a rank as the default rank",
                    " &f● &b/rank setstaff &8(&7name&8) &8(&7value&8) &7| Set a rank as a staff rank",
                    " &f● &b/rank setglobal &8(&7name&8) &8(&7value&8) &7| Set a rank a global rank",
                    " &f● &b/rank sethidden &8(&7name&8) &8(&7value&8) &7| Set a rank as hidden",
                    " &f● &b/rank setcost &8(&7name&8) &8(&7value&8) &7| Set the cost of a rank",
                    " &f● &b/rank setprefix &8(&7name&8) &8(&7prefix&8) &7| Set a rank's prefix",
                    " &f● &b/rank setsuffix &8(&7name&8) &8(&7suffix&8) &7| Set a rank's suffix",
                    " &f● &b/rank setweight &8(&7name&8) &8(&7weight&8) &7| Set a rank's weight",
                    " &f● &b/rank setcolor &8(&7name&8) &8(&7color&8) &7| Set a rank's color",
            },
            {
                    "&b&lRank Commands Help (Page 3):",
                    " &f● &b/rank removepermission &8(&7name&8) &8(&7permission&8) &7| Remove a permission from a rank",
                    " &f● &b/rank addpermission &8(&7name&8) &8(&7permission&8) &7| Add a permission to a rank",
                    " &f● &b/rank listpermissions &8(&7name&8) &7| List all permissions of a rank",
            },
            {
                    "&b&lRank Commands Help (Page 4):",
                    " &f● &b/rank removeserver &8(&7name&8) &8(&7server&8) &7| Remove a server from a rank",
                    " &f● &b/rank addserver &8(&7name&8) &8(&7server&8) &7| Add a server to a rank",
                    " &f● &b/rank listservers &8(&7name&8) &7| List all servers of a rank",
            },
    };

    @Command(name = "rank", permission = "delta.rank.main", description = "Main rank command")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        int page = 1;

        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage(CC.translate("&cInvalid page number! Showing page 1."));
            }
        }

        if (page < 1 || page > pages.length) {
            player.sendMessage(CC.translate("&cPage not found! Showing page 1."));
            page = 1;
        }

        player.sendMessage("");
        for (String line : pages[page - 1]) {
            player.sendMessage(CC.translate(line));
        }
        player.sendMessage(CC.translate("&7Page " + page + " of " + pages.length));
        player.sendMessage("");
    }
}
