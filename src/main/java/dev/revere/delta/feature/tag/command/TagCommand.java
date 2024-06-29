package dev.revere.delta.feature.tag.command;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.tag.menu.TagsMenu;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class TagCommand extends BaseCommand {

    private final String[][] pages = {
            {
                    "&b&lTag Commands Help (Page 1):",
                    " &f● &b/tag view &8(&7name&8) &7| View a tag's information",
                    " &f● &b/tag create &8(&7name&8) &7| Create a tag",
                    " &f● &b/tag delete &8(&7name&8) &7| Delete a tag",
                    " &f● &b/tag menu &7| Open the tag menu",
                    " &f● &b/tag list &7| List all tags",
            },
            {
                    "&b&lTag Commands Help (Page 2):",
                    " &f● &b/tag setpurchaseable &8(&7name&8) &8(&7value&8) &7| Set a tag as purchasable",
                    " &f● &b/tag setcost &8(&7name&8) &8(&7value&8) &7| Set the cost of a tag",
                    " &f● &b/tag setprefix &8(&7name&8) &8(&7prefix&8) &7| Set a tag's prefix",
                    " &f● &b/tag setweight &8(&7name&8) &8(&7weight&8) &7| Set a tag's weight",
                    " &f● &b/tag setcolor &8(&7name&8) &8(&7color&8) &7| Set a tag's color",
            }
    };

    @Command(name = "tag.help", permission = "delta.tag.main", description = "Main tag command")
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

    @Command(name = "tag", inGameOnly = true, description = "Open the tag menu")
    public void onTagCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new TagsMenu().openMenu(player);
    }
}
