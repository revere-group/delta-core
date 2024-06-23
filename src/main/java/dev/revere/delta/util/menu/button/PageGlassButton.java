package dev.revere.delta.util.menu.button;

import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PageGlassButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .durability(6)
                .build();
    }
}
