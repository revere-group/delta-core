package dev.revere.delta.feature.tips.task;

import dev.revere.delta.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/28/2024
 */
@RequiredArgsConstructor
public class TipsTask implements Runnable{

    private final List<String[]> announcements;
    private int currentAnnouncementIndex = 0;

    @Override
    public void run() {
        if (announcements.isEmpty()) {
            return;
        }

        String[] announcement = announcements.get(currentAnnouncementIndex);

        for (Player player : Bukkit.getOnlinePlayers()) {
            for (String line : announcement) {
                player.sendMessage(CC.translate(line.replace("%name%", player.getName())));
            }
        }

        currentAnnouncementIndex = (currentAnnouncementIndex + 1) % announcements.size();
    }
}