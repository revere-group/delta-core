package dev.revere.revsentials.command.player;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.service.ConfigService;
import dev.revere.revsentials.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/20/2024
 */
public class ListCommand extends BaseCommand {
    @Command(name = "list", aliases = {"who", "online"}, description = "List all online players", usage = "/list")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        FileConfiguration config = Revsential.getInstance().getServiceManager().getService(ConfigService.class).getConfigByName("messages.yml");

        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());

        String formattedStaffList = onlinePlayers.stream()
                .filter(p -> p.hasPermission("revsentials.staff"))
                .map(Player::getName)
                .collect(Collectors.joining(", "));

        String formattedPlayerList = onlinePlayers.stream()
                .map(Player::getName)
                .collect(Collectors.joining(", "));

        String totalPlayers = String.valueOf(onlinePlayers.size());
        String maxPlayers = String.valueOf(Bukkit.getMaxPlayers());

        config.getStringList("list-command.lines").forEach(line -> {
            player.sendMessage(CC.translate(replacePlaceholders(line, formattedStaffList, formattedPlayerList, totalPlayers, maxPlayers)));
        });
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
    private String replacePlaceholders(String line, String staffList, String playerList, String totalPlayers, String maxPlayers) {
        return line.replace("%staffs%", staffList)
                .replace("%players%", playerList)
                .replace("%online%", totalPlayers)
                .replace("%max%", maxPlayers);
    }
}
