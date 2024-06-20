package dev.revere.revsentials.command.staff;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Emmy
 * Project: Revsentials
 * Date: 20/06/2024 - 16:31
 */
public class MoreCommand extends BaseCommand {

    @Override
    @Command(name = "more", aliases = {"stackitem"}, permission = "revsentials.more")
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("more-command.no-item-held")));
            return;
        }
        if (item.getAmount() >= 64) {
            player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("more-command.already-a-stack")));
            return;
        }

        item.setAmount(64);
        player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("more-command.given")));
    }
}
