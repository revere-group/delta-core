package dev.revere.delta.feature.tips;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.feature.tips.task.TipsTask;
import dev.revere.delta.service.ConfigService;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/28/2024
 */
@Getter
@Setter
public class TipsService implements IService {

    private final Delta plugin;
    private List<String[]> announcements;

    /**
     * Constructor for the TipsService class
     *
     * @param plugin the main class of the plugin
     */
    public TipsService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        ConfigService configService = plugin.getServiceManager().getService(ConfigService.class);

        boolean tipsEnabled = configService.getConfig("settings.yml").getBoolean("tips.enabled");
        int tipsInterval = configService.getConfig("settings.yml").getInt("tips.interval");

        if (!tipsEnabled) {
            return;
        }

        this.announcements = loadAnnouncements();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new TipsTask(announcements), 20L * tipsInterval, 20L * tipsInterval);
    }

    /**
     * Load all announcements from the config
     *
     * @return a list of announcements
     */
    private List<String[]> loadAnnouncements() {
        ConfigService configService = plugin.getServiceManager().getService(ConfigService.class);
        Map<String, Object> announcementsSection = configService.getConfig("settings.yml").getConfigurationSection("tips.announcements").getValues(false);
        List<String[]> announcements = new ArrayList<>();

        for (Object value : announcementsSection.values()) {
            if (value instanceof List<?> announcementLines) {
                String[] lines = announcementLines.stream().map(Object::toString).toArray(String[]::new);
                announcements.add(lines);
            }
        }

        return announcements;
    }
}
