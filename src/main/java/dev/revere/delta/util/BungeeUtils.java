package dev.revere.delta.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.revere.delta.Delta;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Emmy
 * @project Delta
 * @date 27/06/2024 - 18:12
 */
@UtilityClass
public class BungeeUtils {

    /**
     * Broadcasts a message to all servers.
     *
     * @param player  the player to send the message
     * @param message the message to send
     */
    private void broadcastMessage(Player player, String message) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("MessageRaw");
        dataOutput.writeUTF("ALL");

        dataOutput.writeUTF(message);
        player.sendPluginMessage(Delta.getInstance(), "BungeeCord", dataOutput.toByteArray());
    }

    /**
     * Sends a player to a server.
     *
     * @param player the player to send
     * @param server the server to send the player to
     */
    private void sendPlayer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }
        player.sendMessage(CC.translate("&aJoining &b" + server + "&a..."));
        player.sendPluginMessage(Delta.getInstance(), "BungeeCord", b.toByteArray());
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(CC.translate("&cFailed to connect to &b" + server + "&c!"));
            }
        };
        task.runTaskLater(Delta.getInstance(), 20);
    }
}
