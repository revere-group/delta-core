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
 * @project Delta
 * @date 25/06/2024 - 19:42
 */
public class StrikeCommand extends BaseCommand {
    @Override
    @Command(name = "strike", permission = "delta.command.strike", usage = "strike <player> | all", description = "Strike a player with lightning")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /strike <player> | all"));
            return;
        }

        /*if (args[0].equalsIgnoreCase("all")) {
            player.getServer().getOnlinePlayers().forEach(target -> target.getWorld().strikeLightning(target.getLocation()));

            if (Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getBoolean("trolling.strike-all.broadcast.enabled")) {
                for (String stringList : Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getStringList("trolling.strike-all.broadcast.message")) {
                    player.getServer().getOnlinePlayers().forEach(online -> online.sendMessage(CC.translate(stringList)));
                }
            } else {
                player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("trolling.strike-all.message")));
            }
            return;
        }*/

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        target.getWorld().strikeLightning(target.getLocation());
        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("trolling.strike"))
                .replace("%target%", target.getName()));
    }
}
