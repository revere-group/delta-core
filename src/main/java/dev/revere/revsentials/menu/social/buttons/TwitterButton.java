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
public class TwitterButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.PAPER)
                .name("&b&lTwitter")
                .lore(
                        "",
                        "&7Follow us on Twitter",
                        "&7for updates and giveaways!",
                        ""
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        player.sendMessage(CC.translate("&cThis feature is not yet implemented."));
    }
}
