package dev.revere.delta.command.troll;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Remi
 * @project Delta
 * @date 6/19/2024
 */
public class LaunchCommand extends BaseCommand {
    @Command(name = "launch", permission = "delta.troll.launch", description = "Launch a player", usage = "/launch <player> | all")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /launch <player> | all"));
            return;
        }

        if (args[0].equalsIgnoreCase("all")) {
            player.getServer().getOnlinePlayers().forEach(target -> target.setVelocity(new Vector(0, 1, 0).multiply(15)));
            player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("trolling.launch-all.message")));

            if (Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getBoolean("trolling.launch-all.broadcast.enabled")) {
                for (String stringList : Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getStringList("trolling.launch-all.broadcast.message")) {
                    player.getServer().getOnlinePlayers().forEach(online -> online.sendMessage(CC.translate(stringList)));
                }
            }

            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        target.setVelocity(new Vector(0, 1, 0).multiply(15));
        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("trolling.launch"))
                .replace("%target%", target.getName()));
    }
}
