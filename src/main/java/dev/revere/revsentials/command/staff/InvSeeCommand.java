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
 * Date: 20/06/2024 - 00:23
 */
public class InvSeeCommand extends BaseCommand {
    @Override
    @Command(name = "seeinventory", aliases = {"seeinv", "invsee"}, permission = "revsentials.staff.invsee", description = "See the inventory of a player", usage = "/seeinventory [player]")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /invsee <player>"));
            return;
        }
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        player.openInventory(targetPlayer.getInventory());
        player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("staff.invsee.format"))
                .replace("%target%", targetPlayer.getName()));
    }
}
