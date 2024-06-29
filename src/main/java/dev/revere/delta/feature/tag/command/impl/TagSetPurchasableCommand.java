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
public class TagSetPurchasableCommand extends BaseCommand {
    @Command(name = "tag.setpurchasable", permission = "delta.tag.setpurchasable", inGameOnly = true, description = "Set a tag's purchasable status")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /tag setpurchasable <name> <purchasable>"));
            return;
        }

        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);

        String name = args[0];
        Boolean isPurchasable = getStatus(args, player);
        if (isPurchasable == null) return;

        if (!tagService.tagExists(name)) {
            player.sendMessage(CC.translate("&cA tag with that name does not exist."));
            return;
        }

        Tag tag = tagService.getTag(name);

        tag.setPurchasable(isPurchasable);
        tagService.saveTag(tag);

        player.sendMessage(CC.translate("&fYou have set &b" + tag.getName() + "'s &fpurchasable status to &b" + isPurchasable + "&f."));
    }

    /**
     * Get the status from the command arguments
     *
     * @param args   The command arguments
     * @param player The player executing the command
     * @return The status
     */
    private Boolean getStatus(String[] args, Player player) {
        boolean isPurchasable;
        try {
            isPurchasable = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            player.sendMessage(CC.translate("&cInvalid boolean value."));
            return null;
        }
        return isPurchasable;
    }
}
