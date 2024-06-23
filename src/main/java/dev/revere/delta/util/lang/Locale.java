package dev.revere.delta.util.lang;

import dev.revere.delta.Delta;
import dev.revere.delta.service.ConfigService;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class Locale {
    private static final FileConfiguration messagesConfig = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml");
    private static final FileConfiguration settingsConfig = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("settings.yml");

    public static String SERVER_NAME = settingsConfig.getString("server-name");
    public static String SERVER_REGION = settingsConfig.getString("server-region");
}
