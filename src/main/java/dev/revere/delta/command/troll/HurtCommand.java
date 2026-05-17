package dev.revere.delta.command.troll;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * Project: Delta
 * @date 25/06/2024 - 19:19
 */
public class HurtCommand extends BaseCommand {
    @Override
    @Command(name = "hurt", aliases = {"damage"}, permission = "delta.command.hurt", usage = "hurt (player) [value]", description = "Hurt a player")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /hurt (player) [value]"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        double damage;

        try {
            damage = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        target.damage(damage);
        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("trolling.hurt"))
                .replace("%target%", target.getName())
                .replace("%damage%", String.valueOf(damage)));
    }
}
