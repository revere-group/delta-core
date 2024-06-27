package dev.revere.delta.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.revere.delta.Delta;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 27/06/2024 - 18:12
 */
public class BungeeUtils {
    public static void broadcastMessage(Player player, String message) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("MessageRaw");
        dataOutput.writeUTF("ALL");

        dataOutput.writeUTF(message);
        player.sendPluginMessage(Delta.getInstance(), "BungeeCord", dataOutput.toByteArray());
    }
}
