package dev.revere.revsentials.command.troll;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/19/2024
 */
public class LaunchCommand extends BaseCommand {
    @Command(name = "launch", permission = "revsentials.troll.launch", description = "Launch a player", usage = "/launch <player> | all")
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
            player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("trolling.launch-all.message")));

            if (Revsential.getInstance().getConfig("messages.yml").getBoolean("trolling.launch-all.broadcast.enabled")) {
                for (String stringList : Revsential.getInstance().getConfig("messages.yml").getStringList("trolling.launch-all.broadcast.message")) {
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
        player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("trolling.launch"))
                .replace("%target%", target.getName()));
    }
}
