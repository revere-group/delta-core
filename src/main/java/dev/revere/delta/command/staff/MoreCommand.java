package dev.revere.delta.command.staff;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Emmy
 * @project Delta
 * Date: 20/06/2024 - 16:31
 */
public class MoreCommand extends BaseCommand {

    @Override
    @Command(name = "more", aliases = {"stackitem"}, permission = "delta.more")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("more-command.no-item-held")));
            return;
        }
        if (item.getAmount() >= 64) {
            player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("more-command.already-a-stack")));
            return;
        }

        item.setAmount(64);
        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("more-command.given")));
    }
}
