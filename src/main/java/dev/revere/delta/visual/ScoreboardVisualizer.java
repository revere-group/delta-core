package dev.revere.delta.visual;

import dev.revere.delta.Delta;
import dev.revere.delta.api.color.ColorAPI;
import dev.revere.delta.api.scoreboard.AssembleAdapter;
import dev.revere.delta.feature.combat.CombatLogService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmy
 * @project Delta
 * Date: 20/06/2024 - 10:23
 */
public class ScoreboardVisualizer implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        return ColorAPI.colorizeGradient(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("settings.yml").getString("scoreboard.title"));
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> toReturn = new ArrayList<>();
        List<String> lines = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("settings.yml").getStringList("scoreboard.lines");

        if (lines.isEmpty()) {
            toReturn.add("§7-----------------------------");
            toReturn.add("§cSomething went wrong.");
            toReturn.add("§cTry rejoining the server.");
            toReturn.add("§cIf the issue persists,");
            toReturn.add("§ccontact an admin.");
            toReturn.add("§7-----------------------------");
            return toReturn;
        }

        CombatLogService combatLogService = Delta.getInstance().getServiceManager().getService(CombatLogService.class);

        if (Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("settings.yml").getBoolean("scoreboard.enabled")) {
            for (String line : lines) {
                if (line.contains("%combat-tag%")) {
                    if (combatLogService.isPlayerInCombat(player)) {
                        long remainingTime = combatLogService.getRemainingCombatTime(player);
                        List<String> combatTagLines = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("settings.yml").getStringList("scoreboard.combat-tag.lines");

                        for (String combatLine : combatTagLines) {
                            String formattedCombatLine = CC.translate(combatLine.replace("%tag%", String.valueOf(remainingTime)));
                            toReturn.add(formattedCombatLine);
                        }
                    }
                } else {
                    String formattedLine = CC.translate(line
                            .replace("%online-players%", String.valueOf(player.getServer().getOnlinePlayers().size()))
                            .replace("%max-players%", String.valueOf(player.getServer().getMaxPlayers()))
                            .replace("%deaths%", player.getStatistic(Statistic.DEATHS) + "")
                            .replace("%kills%", player.getStatistic(Statistic.PLAYER_KILLS) + "")
                    );
                    toReturn.add(formattedLine);
                }
            }
        }

        return toReturn;
    }
}
