package dev.revere.revsentials.menu.social.buttons;

import dev.revere.revsentials.util.CC;
import dev.revere.revsentials.util.menu.Button;
import dev.revere.revsentials.util.menu.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class DiscordButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.PAPER)
                .name("&b&lDiscord")
                .lore(
                        "",
                        "&7Join our Discord server",
                        "&7for updates and giveaways!",
                        ""
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.sendMessage(CC.translate("&bDiscord link: https://discord.gg/QsdXsW92sq"));
    }
}
