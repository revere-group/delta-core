package dev.revere.revsentials.command.staff;

import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class ClearInventoryCommand extends BaseCommand {
    @Command(name = "clearinventory", aliases = {"clearinv"}, permission = "revsentials.staff.clearinventory", inGameOnly = true, description = "Clears the inventory of a player", usage = "/clearinventory [player]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.getInventory().setContents(new ItemStack[36]);
            player.getInventory().setArmorContents(new ItemStack[4]);
            player.updateInventory();
            player.sendMessage(CC.translate("&bYour inventory has been cleared."));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        target.getInventory().setContents(new ItemStack[36]);
        target.getInventory().setArmorContents(new ItemStack[4]);
        target.sendMessage(CC.translate("&fYour inventory has been cleared by &b" + player.getName() + "&f."));
        target.updateInventory();
    }
}
