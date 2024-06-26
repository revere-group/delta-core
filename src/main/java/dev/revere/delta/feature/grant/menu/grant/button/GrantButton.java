package dev.revere.delta.feature.grant.menu.grant.button;

import dev.revere.delta.util.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Delta
 * @date 26/06/2024 - 23:33
 */
@AllArgsConstructor
public class GrantButton extends Button {

    private OfflinePlayer target;

    @Override
    public ItemStack getButtonItem(Player player) {
        return null;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

    }
}
