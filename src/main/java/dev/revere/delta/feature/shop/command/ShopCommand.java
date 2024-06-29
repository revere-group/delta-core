package dev.revere.delta.feature.shop.command;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.shop.menus.CoinShopMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 19:21
 */
public class ShopCommand extends BaseCommand {
    @Override
    @Command(name = "shop", aliases = {"store", "buy"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new CoinShopMenu().openMenu(player);
    }
}
