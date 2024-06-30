package dev.revere.delta.feature.advancement.button;

import dev.revere.delta.feature.advancement.menu.AdvancementsMenu;
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
public class AdvancementProfileButton extends Button {
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
}