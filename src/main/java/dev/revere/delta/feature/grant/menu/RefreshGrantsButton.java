package dev.revere.delta.feature.grant.menu;

import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
@AllArgsConstructor
public class RefreshGrantsButton extends Button {

    private boolean showingActiveGrants;
    private OfflinePlayer target;

    /**
     * Gets the item stack for the button.
     *
     * @param player the player viewing the button
     * @return the item stack
     */
    @Override
    public ItemStack getButtonItem(Player player) {
        return showingActiveGrants ? new ItemBuilder(Material.LIME_WOOL).name("&aShow Inactive Grants").build() : new ItemBuilder(Material.RED_WOOL).name("&cShow Active Grants").build();
    }

    /**
     * Handles the click event for the button.
     *
     * @param player      the player who clicked the button
     * @param slot        the slot the button was clicked in
     * @param clickType   the type of click
     * @param hotbarButton the hotbar button clicked
     */
    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        playNeutral(player);
        showingActiveGrants = !showingActiveGrants;
        new GrantMenu(showingActiveGrants, target).openMenu(player);
    }
}