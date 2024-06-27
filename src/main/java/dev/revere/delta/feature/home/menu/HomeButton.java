package dev.revere.delta.feature.home.menu;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.home.Home;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Delta
 * @date 6/18/2024
 */
@AllArgsConstructor
public class HomeButton extends Button {

    private final Home home;

    @Override
    public ItemStack getButtonItem(Player player) {
        FileConfiguration config = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("menus/homes-menu.yml");
        return new ItemBuilder(Material.matchMaterial(config.getString("home-button.material")))
                .name(config.getString("home-button.name"))
                .lore(config.getStringList("home-button.lore")
                        .stream()
                        .map(line -> line
                                .replace("%name%", StringUtils.capitalize(home.getName()))
                                .replace("%x%", String.valueOf(home.getLocation().getBlockX()))
                                .replace("%y%", String.valueOf(home.getLocation().getBlockY()))
                                .replace("%z%", String.valueOf(home.getLocation().getBlockZ()))
                                .replace("%world%", home.getLocation().getWorld().getName()))
                        .toArray(String[]::new))
                .durability(config.getInt("home-button.data"))
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        switch (clickType) {
            case LEFT:
                Delta.getInstance().getHomeRepository().teleportHome(player, home.getName());
                break;
            case RIGHT:
                Delta.getInstance().getHomeRepository().removeHome(player, home.getName());
                break;
        }
    }
}
