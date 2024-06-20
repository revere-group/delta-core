package dev.revere.revsentials.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Revsentials
 * Date: 20/06/2024 - 01:32
 */
public class ServerUtil {
    public static void disconnectPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(CC.translate("&cServer is restarting, please rejoin in a few minutes."));
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4Kicked all players due to a server restart."));
        }
    }
}
