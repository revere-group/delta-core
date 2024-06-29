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
public class TagSetCostCommand extends BaseCommand {
    @Command(name = "tag.setcost", permission = "delta.tag.setcost", inGameOnly = true, description = "Set a tag's cost")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /tag setcost <name> <cost>"));
            return;
        }

        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);

        String name = args[0];
        Integer cost = getCost(args, player);
        if (cost == null) return;

        if (!tagService.tagExists(name)) {
            player.sendMessage(CC.translate("&cA tag with that name does not exist."));
            return;
        }

        Tag tag = tagService.getTag(name);
        tag.setCost(cost);
        tagService.saveTag(tag);

        player.sendMessage(CC.translate("&fSuccessfully set the cost of the tag &b" + name + " &fto &b" + cost + "&f."));
    }


    /**
     * Get the cost from the command arguments
     *
     * @param args   The command arguments
     * @param player The player executing the command
     * @return The cost
     */
    private Integer getCost(String[] args, Player player) {
        int cost;
        try {
            cost = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cThe cost must be a number."));
            return null;
        }
        return cost;
    }
}
