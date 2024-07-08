package dev.revere.delta.visual;

import dev.revere.delta.Delta;
import dev.revere.delta.api.scoreboard.AssembleAdapter;
import dev.revere.delta.feature.combat.CombatLogService;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Delta
 * @date 20/06/2024 - 10:23
 */
public class ScoreboardVisualizer implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        boolean smallFont = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("settings.yml").getBoolean("scoreboard.small-font");
        String title = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("settings.yml").getString("scoreboard.title");

        if (smallFont) {
            title = CC.toSmallFont(CC.translate(title));
        }

        return title;
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
                        boolean smallFont = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("settings.yml").getBoolean("scoreboard.small-font");
                        long remainingTime = combatLogService.getRemainingCombatTime(player);
                        List<String> combatTagLines = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("settings.yml").getStringList("scoreboard.combat-tag.lines");

                        for (String combatLine : combatTagLines) {
                            String formattedCombatLine = CC.translate(combatLine.replace("%tag%", String.valueOf(remainingTime)));

                            if (smallFont) {
                                formattedCombatLine = CC.toSmallFont(CC.translate(formattedCombatLine));
                            }

                            toReturn.add(formattedCombatLine);
                        }
                    }
                } else {
                    Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());
                    RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);
                    boolean smallFont = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("settings.yml").getBoolean("scoreboard.small-font");
                    String formattedLine = CC.translate(line
                            .replace("%online-players%", String.valueOf(player.getServer().getOnlinePlayers().size()))
                            .replace("%max-players%", String.valueOf(player.getServer().getMaxPlayers()))
                            .replace("%deaths%", player.getStatistic(Statistic.DEATHS) + "")
                            .replace("%kills%", player.getStatistic(Statistic.PLAYER_KILLS) + "")
                            .replace("%ping%", player.getPing() + "")
                            .replace("%coins%", profile.getCoins() + "")
                            .replace("%rank%", rankService.getHighestRank(profile).getName())
                            .replace("%rank-color%", rankService.getHighestRank(profile).getNameColor().toString())
                            .replace("%rank-prefix%", rankService.getHighestRank(profile).getPrefix()
                    ));

                    if (smallFont) {
                        formattedLine = CC.toSmallFont(CC.translate(formattedLine));
                    }

                    toReturn.add(formattedLine);
                }
            }
        }

        return Delta.getInstance().isPlaceholderAPIEnabled() ? PlaceholderAPI.setPlaceholders(player, toReturn) : toReturn;
    }
}
