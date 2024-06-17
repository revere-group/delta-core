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
public class WebsiteButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.PAPER)
                .name("&b&lWebsite")
                .lore(
                        "",
                        "&7Visit our website to",
                        "&7learn more about us!",
                        ""
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.sendMessage(CC.translate("&bWebsite link: https://www.revere.dev"));
    }
}
