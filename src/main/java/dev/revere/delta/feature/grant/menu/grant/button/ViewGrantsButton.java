package dev.revere.delta.feature.grant.menu.grant.button;

import dev.revere.delta.feature.grant.menu.grants.GrantsMenu;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Delta
 * @date 26/06/2024 - 23:38
 */
public class ViewGrantsButton extends Button {

    private OfflinePlayer target;

    private final String name;
    private final Material material;
    private final int data;
    private final List<String> lore;

    public ViewGrantsButton(OfflinePlayer target, String name, Material material, int data, List<String> lore) {
        this.target = target;
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

        new GrantsMenu(true, target).openMenu(player);
    }
}
