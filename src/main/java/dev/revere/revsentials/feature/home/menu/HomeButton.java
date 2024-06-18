package dev.revere.revsentials.feature.home.menu;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.feature.home.Home;
import dev.revere.revsentials.util.menu.Button;
import dev.revere.revsentials.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/18/2024
 */
@AllArgsConstructor
public class HomeButton extends Button {

    private final Home home;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.BLACK_BED)
                .name("&b&lHome Information:")
                .lore(" &f● &bName: &7" + StringUtils.capitalize(home.getName()))
                .lore(" &f● &bLocation: &7" + home.getLocation().getBlockX() + ", " + home.getLocation().getBlockY() + ", " + home.getLocation().getBlockZ())
                .lore(" &f● &bWorld: &7" + home.getLocation().getWorld().getName())
                .lore("")
                .lore("&b&lActions:")
                .lore(" &f● &bLeft Click &7| &fTeleport to home")
                .lore(" &f● &bRight Click &7| &fDelete home")
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        switch (clickType) {
            case LEFT:
                Revsential.getInstance().getHomeRepository().teleportHome(player, home.getName());
                break;
            case RIGHT:
                Revsential.getInstance().getHomeRepository().removeHome(player, home.getName());
                break;
        }
    }
}
