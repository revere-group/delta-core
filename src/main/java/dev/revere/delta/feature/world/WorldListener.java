package dev.revere.delta.feature.world;

import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class WorldListener implements Listener {


    private void broadcastDayMessage() {
        String[] messages = {
                "The sun is rising, time to seize the day!",
                "Good morning! Time to start a new adventure!",
                "Wake up and smell the roses, it's day time!"
        };

        String message = messages[(int) (Math.random() * messages.length)];
        Bukkit.broadcastMessage(CC.translate("&f" + message));
    }

    private void broadcastNightMessage() {
        String[] messages = {
                "Night has fallen, beware of the monsters!",
                "Sleep tight, don't let the creepers bite!",
                "The moon is up, time for some night-time mining!"
        };

        String message = messages[(int) (Math.random() * messages.length)];
        Bukkit.broadcastMessage(CC.translate("&f" + message));
    }
}
