package dev.revere.revsentials.util.lang;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.service.ConfigService;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class Locale {
    private static final FileConfiguration messagesConfig = Revsential.getInstance().getServiceManager().getService(ConfigService.class).getConfigByName("messages.yml");
    private static final FileConfiguration settingsConfig = Revsential.getInstance().getServiceManager().getService(ConfigService.class).getConfigByName("settings.yml");

    public static String SERVER_NAME = settingsConfig.getString("server-name");
    public static String SERVER_REGION = settingsConfig.getString("server-region");
}
