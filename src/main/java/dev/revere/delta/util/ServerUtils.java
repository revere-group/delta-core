package dev.revere.delta.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * @project Delta
 * Date: 20/06/2024 - 01:32
 */
public class ServerUtils {
    public static void disconnectPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(CC.translate("&cServer is restarting, please rejoin in a few minutes."));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4Kicked all players due to a server restart."));
        }
    }
}
