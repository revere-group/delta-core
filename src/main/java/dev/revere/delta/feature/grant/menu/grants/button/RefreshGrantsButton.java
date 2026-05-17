package dev.revere.delta.feature.grant.menu.grants.button;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.grant.menu.grants.GrantsMenu;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
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
        FileConfiguration config = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("menus/grants-menu.yml");
        return showingActiveGrants ? inactiveButton(config) : activeButton(config);
    }

    /**
     * Gets the item stack for the inactive button.
     *
     * @param config the config file
     * @return the item stack
     */
    private ItemStack inactiveButton(FileConfiguration config) {
        return new ItemBuilder(Material.matchMaterial(config.getString("refresh-button.show-inactive.material")))
                .name(config.getString("refresh-button.show-inactive.name"))
                .lore(config.getStringList("refresh-button.show-inactive.lore"))
                .durability(config.getInt("refresh-button.show-inactive.data"))
                .build();
    }

    /**
     * Gets the item stack for the active button.
     *
     * @param config the config file
     * @return the item stack
     */
    private ItemStack activeButton(FileConfiguration config) {
        return new ItemBuilder(Material.matchMaterial(config.getString("refresh-button.show-active.material")))
                .name(config.getString("refresh-button.show-active.name"))
                .lore(config.getStringList("refresh-button.show-active.lore"))
                .durability(config.getInt("refresh-button.show-active.data"))
                .build();
    }

    /**
     * Handles the click event for the button.
     *
     * @param player       the player who clicked the button
     * @param slot         the slot the button was clicked in
     * @param clickType    the type of click
     * @param hotbarButton the hotbar button clicked
     */
    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        if (clickType != ClickType.LEFT) return;
        playNeutral(player);
        showingActiveGrants = !showingActiveGrants;
        new GrantsMenu(showingActiveGrants, target).openMenu(player);
    }
}