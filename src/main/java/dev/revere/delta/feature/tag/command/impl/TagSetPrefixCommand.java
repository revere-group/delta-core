package dev.revere.delta.feature.tag.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class TagSetPrefixCommand extends BaseCommand {
    @Command(name = "tag.setprefix", permission = "delta.tag.setprefix", inGameOnly = true, description = "Set a tag's prefix")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /tag setprefix <name> <prefix>"));
            return;
        }

        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);

        String name = args[0];
        String prefix = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        if (!tagService.tagExists(name)) {
            player.sendMessage(CC.translate("&cA tag with that name does not exist."));
            return;
        }

        Tag tag = tagService.getTag(name);
        tag.setPrefix(prefix);
        tagService.saveTag(tag);

        player.sendMessage(CC.translate("&fSuccessfully set the prefix of the tag &b" + name + " &fto &b" + prefix + "&f."));
    }
}
