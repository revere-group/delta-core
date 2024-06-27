package dev.revere.delta.command.staff;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class ClearInventoryCommand extends BaseCommand {
    @Command(name = "clearinventory", aliases = {"clearinv", "clear"}, permission = "delta.staff.clearinventory", inGameOnly = true, description = "Clears the inventory of a player", usage = "/clearinventory [player]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.getInventory().setContents(new ItemStack[36]);
            player.getInventory().setArmorContents(new ItemStack[4]);
            player.updateInventory();
            player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.cleared-inventory")));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        target.getInventory().setContents(new ItemStack[36]);
        target.getInventory().setArmorContents(new ItemStack[4]);
        target.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("player.inventory-cleared-by"))
                .replace("%player%", player.getName()));
        target.updateInventory();
    }
}
