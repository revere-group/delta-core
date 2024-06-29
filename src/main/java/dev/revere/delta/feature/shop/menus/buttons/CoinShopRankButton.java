package dev.revere.delta.feature.shop.menus.buttons;

import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 19:43
 */
@AllArgsConstructor
public class CoinShopRankButton extends Button {

    private Rank rank;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.LIME_WOOL)
                .name("&b" + rank.getName())
                .lore(Arrays.asList(
                        "",
                        "&b&lRank Details:",
                        " &f● &bDisplay Name: &f" + rank.getNameColor() + rank.getName(),
                        " &f● &bPrefix: &f" + rank.getPrefix(),
                        " &f● &bSuffix: &f" + rank.getSuffix(),
                        " &f● &bPriority: &f" + rank.getWeight(),
                        " &f● &bColor: &f" + rank.getNameColor() + rank.getNameColor().name(),
                        "",
                        "&fPrice: &b$" + rank.getCost(),
                        "",
                        "&f► Click to &bpurchase &fthis rank!",
                        ""))
                .hideMeta()
                .build();
    }
}
