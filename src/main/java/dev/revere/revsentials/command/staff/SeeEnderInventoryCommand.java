package dev.revere.revsentials.command.staff;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Revsentials
 * Date: 20/06/2024 - 20:12
 */
public class SeeEnderInventoryCommand extends BaseCommand {
    @Override
    @Command(name = "endinvsee", aliases = {"endinv", "seeendinv", "enderinv", "seeinvender"}, permission = "revsentials.staff.invsee.end", description = "End the inventory see session", usage = "/endinvsee")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /seeinvender (player)"));
            return;
        }

        String targetName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetName);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        player.openInventory(targetPlayer.getEnderChest());
        player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("staff.endinvsee.format"))
                .replace("%target%", targetPlayer.getName())
        );
    }
}
