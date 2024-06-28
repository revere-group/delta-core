package dev.revere.delta.command.staff.coins;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/27/2024
 */
public class CoinsManagerCommand extends BaseCommand {
    @Command(name = "coinsmanager", aliases = {"cmanager", "coinsm", "cm"}, permission = "delta.staff.coins.manage")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lCoins Manager Help:"));
        player.sendMessage(CC.translate(" &f● &b/coinsmanager give &8(&7player&8) &8(&7amount&8) &7| Give coins to a player"));
        player.sendMessage(CC.translate(" &f● &b/coinsmanager take &8(&7player&8) &8(&7amount&8) &7| Take coins from a player"));
        player.sendMessage(CC.translate(" &f● &b/coinsmanager set &8(&7player&8) &8(&7amount&8) &7| Set a player's coins"));
        player.sendMessage(CC.translate(" &f● &b/coinsmanager reset &8(&7player&8) &7| Reset a player's coins"));
        player.sendMessage(CC.translate(" &f● &b/coinsmanager view &8(&7player&8) &7| View a player's coins"));
        player.sendMessage("");
    }
}
