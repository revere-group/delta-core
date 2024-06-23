package dev.revere.delta.command.staff;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 *
 * @project Delta
 * Date: 20/06/2024 - 00:23
 */
public class InvSeeCommand extends BaseCommand {
    @Override
    @Command(name = "seeinventory", aliases = {"seeinv", "invsee"}, permission = "delta.staff.invsee", description = "See the inventory of a player", usage = "/seeinventory [player]")
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
        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.invsee.format"))
                .replace("%target%", targetPlayer.getName()));
    }
}
