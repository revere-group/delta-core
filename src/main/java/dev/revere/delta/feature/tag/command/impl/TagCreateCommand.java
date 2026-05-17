package dev.revere.delta.feature.tag.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class TagCreateCommand extends BaseCommand {
    @Command(name = "tag.create", permission = "delta.tag.create", inGameOnly = true, description = "Create a tag")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /tag create <name>"));
            return;
        }

        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);

        String name = args[0];
        if (tagService.tagExists(name)) {
            player.sendMessage(CC.translate("&cA tag with that name already exists."));
            return;
        }

        Tag tag = new Tag(name);
        tag.setPrefix(name);
        tag.setWeight(0);
        tag.setCost(0);
        tag.setPurchasable(false);
        tagService.createTag(tag);

        player.sendMessage(CC.translate("&fSuccessfully created the tag &b" + name + "&f."));
    }
}
