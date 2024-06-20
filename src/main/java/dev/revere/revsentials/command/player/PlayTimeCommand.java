package dev.revere.revsentials.command.player;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import dev.revere.revsentials.util.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class PlayTimeCommand extends BaseCommand {
    @Command(name = "playtime", aliases = {"pt"}, inGameOnly = true, description = "Shows your playtime", usage = "/playtime [player]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        String lookFor = "PLAY_ONE_MINUTE";

        if (args.length == 0) {
            long playTime = player.getStatistic(Statistic.valueOf(lookFor));
            player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("player.playtime"))
                    .replace("%playtime%", DateUtils.formatTimeMillis(playTime * 50L)));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        long playTime = target.getStatistic(Statistic.valueOf(lookFor));
        player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("player.playtime-target"))
                .replace("%target%", target.getName())
                .replace("%playtime%", DateUtils.formatTimeMillis(playTime * 50L))
        );

    }
}
