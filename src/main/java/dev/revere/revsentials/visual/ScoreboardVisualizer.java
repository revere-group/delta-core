package dev.revere.revsentials.visual;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.util.CC;
import dev.revere.revsentials.api.scoreboard.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmy
 * Project: Revsentials
 * Date: 20/06/2024 - 10:23
 */
public class ScoreboardVisualizer implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        return CC.translate(Revsential.getInstance().getConfig("settings.yml").getString("scoreboard.title"));
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> toReturn = new ArrayList<>();
        List<String> lines = Revsential.getInstance().getConfig("settings.yml").getStringList("scoreboard.lines");

        if (lines.isEmpty()) {
            toReturn.add("§7-----------------------------");
            toReturn.add("§cSomething went wrong.");
            toReturn.add("§cTry rejoining the server.");
            toReturn.add("§cIf the issue persists,");
            toReturn.add("§ccontact an admin.");
            toReturn.add("§7-----------------------------");
            return toReturn;
        }

        if (Revsential.getInstance().getConfig("settings.yml").getBoolean("scoreboard.enabled")) {
            for (String line : lines) {
                String formattedLine = CC.translate(line
                        .replace("%online-players%", String.valueOf(player.getServer().getOnlinePlayers().size()))
                        .replace("%max-players%", String.valueOf(player.getServer().getMaxPlayers()))
                        .replace("%deaths%", player.getStatistic(org.bukkit.Statistic.DEATHS) + "")
                        .replace("%kills%", player.getStatistic(org.bukkit.Statistic.PLAYER_KILLS) + "")
                );

                toReturn.add(formattedLine);
            }
        }

        return toReturn;
    }
}
