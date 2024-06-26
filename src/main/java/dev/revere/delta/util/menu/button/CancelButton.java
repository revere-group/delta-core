package dev.revere.delta.util.menu.button;

import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Delta
 * @date 26/06/2024 - 23:18
 */
public class CancelButton extends Button {
    private final String name;
    private final Material material;
    private final int data;
    private final List<String> lore;

    public CancelButton(String name, Material material, int data, List<String> lore) {
        this.name = name;
        this.material = material;
        this.data = data;
        this.lore = lore;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(material)
                .name(name)
                .durability(data)
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;
        player.closeInventory();
    }
}