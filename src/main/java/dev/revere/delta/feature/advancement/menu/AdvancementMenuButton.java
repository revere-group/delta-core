package dev.revere.delta.feature.advancement.menu;

import dev.revere.delta.feature.advancement.AdvancementCategory;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 30/06/2024 - 18:18
 */
public class AdvancementMenuButton extends Button {
    private final String name;
    private final ItemStack material;
    private final List<String> lore;
    private final AdvancementCategory category;

    public AdvancementMenuButton(String name, ItemStack material, List<String> lore, AdvancementCategory category) {
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.category = category;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(material)
                .name(name)
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        new CategoryMenu(category).openMenu(player);
    }
}