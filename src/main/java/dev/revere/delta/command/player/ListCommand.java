package dev.revere.delta.command.player;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project Delta
 * @date 6/20/2024
 */
public class ListCommand extends BaseCommand {
    @Command(name = "list", aliases = {"who", "online"}, description = "List all online players", usage = "/list")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        FileConfiguration config = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml");
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (!isVanished(player)) {
            onlinePlayers = onlinePlayers.stream()
                    .filter(p -> !isVanished(p))
                    .toList();
        }
        ;

        String formattedStaffList = onlinePlayers.stream()
                .filter(p -> {
                    Profile profile = profileService.getProfile(p.getUniqueId());
                    Rank rank = rankService.getHighestRank(profile);
                    return rank.isStaffRank();
                })
                .map(p -> {
                    Profile profile = profileService.getProfile(p.getUniqueId());
                    Rank rank = rankService.getHighestRank(profile);
                    return rank.getNameColor().toString() + p.getName();
                })
                .collect(Collectors.joining(", "));

        if (formattedStaffList.isEmpty()) {
            formattedStaffList = config.getString("list-command.no-staff");
        }

        String formattedRankList = Delta.getInstance().getServiceManager().getService(RankService.class).getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .map(rank -> rank.getNameColor() + rank.getName())
                .collect(Collectors.joining(", "));

        String formattedRankListPrefix = Delta.getInstance().getServiceManager().getService(RankService.class).getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .map(Rank::getPrefix)
                .collect(Collectors.joining(", "));

        List<String> formattedPlayerList = onlinePlayers.stream()
                .sorted(Comparator.comparingInt(p -> {
                    Profile profile = profileService.getProfile(p.getUniqueId());
                    Rank rank = rankService.getHighestRank(profile);
                    return -rank.getWeight();
                }))
                .map(p -> {
                    Profile profile = profileService.getProfile(p.getUniqueId());
                    Rank rank = rankService.getHighestRank(profile);
                    return rank.getNameColor() + p.getName();
                })
                .collect(Collectors.toList());

        String formattedRankListPrefixString = String.join(", ", formattedRankListPrefix);
        String formattedPlayerListString = String.join(", ", formattedPlayerList);
        String formattedStaffListString = String.join(", ", formattedStaffList);
        String formattedRankListString = String.join(", ", formattedRankList);

        String totalPlayers = String.valueOf(onlinePlayers.size());
        String maxPlayers = String.valueOf(Bukkit.getMaxPlayers());

        config.getStringList("list-command.lines").forEach(line -> {
            player.sendMessage(CC.translate(replacePlaceholders(line, formattedRankListPrefixString, formattedRankListString, formattedStaffListString, formattedPlayerListString, totalPlayers, maxPlayers)));
        });
    }

    /**
     * Checks if a player is in vanish.
     *
     * @param player the player to check
     * @return true if the player is in vanish, false otherwise
     */
    private boolean isVanished(Player player) {
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());
        return profile.getStaffOptions().isVanish();
    }

    /**
     * Replaces placeholders in the given line with the provided values
     *
     * @param line         the line to replace placeholders in
     * @param staffList    the list of staff members
     * @param playerList   the list of players
     * @param totalPlayers the total amount of online players
     * @param maxPlayers   the maximum amount of players
     * @return the line with placeholders replaced
     */
    private String replacePlaceholders(String line, String rankListPrefix, String rankList, String staffList, String playerList, String totalPlayers, String maxPlayers) {
        return line.replace("%ranks-prefix%", rankListPrefix)
                .replace("%ranks%", rankList)
                .replace("%staffs%", staffList)
                .replace("%players%", playerList)
                .replace("%online%", totalPlayers)
                .replace("%max%", maxPlayers);
    }
}
