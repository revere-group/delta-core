package dev.revere.delta.feature.tag.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
import dev.revere.delta.util.CC;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Comparator;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class TagListCommand extends BaseCommand {
    @Command(name = "tag.list", permission = "delta.tag.list", inGameOnly = true, description = "List all tags")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);

        player.sendMessage("");
        player.sendMessage(CC.translate("     &b&lTag List &f(" + tagService.getTags().size() + "&f)"));
        if (tagService.getTags().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo tags available."));
        } else {
            tagService.getTags().stream()
                    .sorted(Comparator.comparingInt(Tag::getWeight).reversed())
                    .forEach(tag -> {
                        BaseComponent[] hoverText = new ComponentBuilder("")
                                .append(CC.translate("&b&lTag Information:\n"))
                                .append(CC.translate(" &f● Prefix: "))
                                .append(CC.translate(tag.getPrefix())).append("\n")
                                .append(CC.translate(" &f● Weight: "))
                                .append(CC.translate("&b" + tag.getWeight())).append("\n")
                                .append(CC.translate(" &f● Cost: "))
                                .append(CC.translate("&b" + tag.getCost())).append("\n")
                                .append(CC.translate(" &f● Color: "))
                                .append(CC.translate(tag.getColor() + tag.getColor().name())).append("\n")
                                .append(CC.translate(" &f● Purchasable: "))
                                .append(CC.translate("&b" + (tag.isPurchasable() ? "Yes" : "No"))).append("\n")
                                .create();

                        ComponentBuilder message = new ComponentBuilder("      ");
                        message.append("● ").color(ChatColor.WHITE.asBungee());
                        message.append(tag.getName()).color(ChatColor.AQUA.asBungee());
                        message.append(" - ").color(ChatColor.WHITE.asBungee());
                        message.append(CC.translate(tag.getPrefix()));
                        message.append(" ").color(ChatColor.DARK_GRAY.asBungee());
                        message.append("(" + tag.getWeight() + ")").color(ChatColor.DARK_GRAY.asBungee());

                        message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));

                        player.spigot().sendMessage(message.create());
                    });
        }

        player.sendMessage("");
    }
}
