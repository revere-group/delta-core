package dev.revere.delta.util.menu.button;

import dev.revere.delta.util.CC;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class BackButton extends Button {

    private Menu back;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.ARROW)
                .name("&b&lGo Back")
                .durability(0)
                .lore(Arrays.asList(
                        CC.translate("&8&m----------------------"),
                        CC.translate(" &fThis allows you to return"),
                        CC.translate(" &fback to your previous menu"),
                        CC.translate(" "),
                        CC.translate("&aClick to go back"),
                        CC.translate("&8&m----------------------")
                ))
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Button.playNeutral(player);
        back.openMenu(player);
    }
}
