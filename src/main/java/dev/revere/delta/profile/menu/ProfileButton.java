package dev.revere.delta.profile.menu;

import dev.revere.delta.feature.advancement.menu.AdvancementsMenu;
import dev.revere.delta.feature.shop.menus.CoinShopMenu;
import dev.revere.delta.feature.tag.menu.TagsMenu;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/29/2024
 */
@AllArgsConstructor
public class ProfileButton extends Button {
    private String displayName;
    private ItemStack itemStack;
    private List<String> lore;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(itemStack)
                .name(displayName)
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }

        Material material = itemStack.getType();

        switch (material) {
            case EMERALD:
                new CoinShopMenu().openMenu(player);
                break;
            case NAME_TAG:
                new TagsMenu().openMenu(player);
                break;
            case BOOK:
                new AdvancementsMenu().openMenu(player);
        }
        playNeutral(player);
    }
}