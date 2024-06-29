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
public class TagViewCommand extends BaseCommand {
    @Command(name = "tag.view", permission = "delta.tag.view", inGameOnly = true, description = "View a tag's information")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /tag view <name>"));
            return;
        }

        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);

        String name = args[0];
        if (!tagService.tagExists(name)) {
            player.sendMessage(CC.translate("&cA tag with that name does not exist."));
            return;
        }

        Tag tag = tagService.getTag(name);
        player.sendMessage("");
        player.sendMessage(CC.translate("&b&l" + tag.getName() + " &7Information:"));
        player.sendMessage(CC.translate(" &f● &fName: &b" + tag.getName()));
        player.sendMessage(CC.translate(" &f● &fPrefix: &b" + tag.getPrefix()));
        player.sendMessage(CC.translate(" &f● &fWeight: &b" + tag.getWeight()));
        player.sendMessage(CC.translate(" &f● &fCost: &b" + tag.getCost()));
        player.sendMessage(CC.translate(" &f● &fColor: &b" + tag.getColor() + tag.getColor().name()));
        player.sendMessage(CC.translate(" &f● &fPurchasable: &b" + (tag.isPurchasable() ? "Yes" : "No")));
        player.sendMessage("");
    }
}
