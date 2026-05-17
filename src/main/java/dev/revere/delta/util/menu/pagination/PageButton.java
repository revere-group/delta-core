package dev.revere.delta.util.menu.pagination;

import dev.revere.delta.util.CC;
import dev.revere.delta.util.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class PageButton extends Button {

    private int mod;
    private PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                return new ItemBuilder(Material.LEVER)
                        .name(CC.translate("&b&lNext Page"))
                        .lore(Arrays.asList(
                                CC.translate("&8&m----------------------"),
                                CC.translate(" &fPage: &b" + (this.menu.getPage() + this.mod) + "&f/&b" + this.menu.getPages(player)),
                                CC.translate(" "),
                                CC.translate("&aClick to view the next page"),
                                CC.translate("&8&m----------------------")
                        ))
                        .clearFlags()
                        .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                        .name(CC.translate("&b&lNext Page"))
                        .lore(Arrays.asList(
                                CC.translate("&8&m----------------------"),
                                CC.translate(" "),
                                CC.translate("&cThere are no available next pages"),
                                CC.translate("&8&m----------------------")
                        ))
                        .clearFlags()
                        .build();
            }
        } else {
            if (hasPrevious(player)) {
                return new ItemBuilder(Material.LEVER)
                        .name(CC.translate("&b&lPrevious Page"))
                        .lore(Arrays.asList(
                                CC.translate("&8&m----------------------"),
                                CC.translate(" &fPage: &b" + (this.menu.getPage() + this.mod) + "&f/&b" + this.menu.getPages(player)),
                                CC.translate(" "),
                                CC.translate("&aClick to view the previous page"),
                                CC.translate("&8&m----------------------")
                        ))
                        .clearFlags()
                        .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                        .name(CC.translate("&b&lPrevious Page"))
                        .lore(Arrays.asList(
                                CC.translate("&8&m----------------------"),
                                CC.translate(" "),
                                CC.translate("&cThere are no available previous pages"),
                                CC.translate("&8&m----------------------")
                        ))
                        .clearFlags()
                        .build();
            }
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                this.menu.modPage(player, this.mod);
                Button.playNeutral(player);
            } else {
                Button.playFail(player);
            }
        } else {
            if (hasPrevious(player)) {
                this.menu.modPage(player, this.mod);
                Button.playNeutral(player);
            } else {
                Button.playFail(player);
            }
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return this.menu.getPages(player) >= pg;
    }

    private boolean hasPrevious(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0;
    }

}
