package dev.revere.revsentials.command.staff;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class HealCommand extends BaseCommand {
    @Command(name = "heal", aliases = {"heal"}, permission = "revsentials.staff.heal", inGameOnly = true, description = "Heals a player", usage = "/heal [player]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.setHealth(20);
            player.setFoodLevel(20);
            player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("staff.healed.format")));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        target.setHealth(20);
        target.setFoodLevel(20);
        target.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("player.healed-by"))
                .replace("%player%", player.getName()));
    }
}
