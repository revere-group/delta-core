package dev.revere.delta.feature.tag.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
import dev.revere.delta.util.CC;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class TagSetColorCommand extends BaseCommand {
    @Command(name = "tag.setcolor", permission = "delta.tag.setcolor", inGameOnly = true, description = "Set a tag's color")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /tag setcolor <name> <color>"));
            return;
        }

        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);

        String name = args[0];
        String color = args[1];

        if (!tagService.tagExists(name)) {
            player.sendMessage(CC.translate("&cA tag with that name does not exist."));
            return;
        }

        String translatedColor = CC.translateColor(color);
        if (translatedColor == null) {
            player.sendMessage(CC.translate("&cInvalid color input. Please use a valid color name or hex format (e.g., <#RRGGBB>)."));
            return;
        }

        ChatColor chatColor = ChatColor.of(translatedColor);
        if (chatColor == null) {
            player.sendMessage(CC.translate("&cInvalid color input. Please use a valid color name or hex format (e.g., <#RRGGBB>)."));
            return;
        }

        Tag tag = tagService.getTag(name);
        tag.setColor(chatColor);
        tagService.saveTag(tag);

        player.sendMessage(CC.translate("&fSuccessfully set the color of the tag &b" + name + " &fto " + chatColor + translatedColor + "&f."));
    }
}
