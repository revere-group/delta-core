package dev.revere.revsentials.command.staff;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Emmy
 * Project: Revsentials
 * Date: 20/06/2024 - 09:42
 */
public class ListCommand extends BaseCommand {

    @Override
    @Command(name = "list", aliases = {"who", "online"}, inGameOnly = true, description = "List all online players")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        FileConfiguration config = Revsential.getInstance().getConfig("messages.yml");

        String separatorFormat = config.getString("list-command.separator-format", "&f, &b");
        List<String> messageFormat = config.getStringList("list-command.message");

        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        String playerList = onlinePlayers.stream()
                .map(p -> (p.isOp() ? config.getString("list-command.colors.operators", "&c") : config.getString("list-command.colors.players", "&f")) + p.getName())
                .collect(Collectors.joining(CC.translate(separatorFormat)));

        for (String line : messageFormat) {
            player.sendMessage(CC.translate(line
                    .replace("%online-players%", String.valueOf(onlinePlayers.size()))
                    .replace("%max-players%", String.valueOf(Bukkit.getMaxPlayers()))
                    .replace("%player-list%", playerList)
            ));
        }
    }
}
