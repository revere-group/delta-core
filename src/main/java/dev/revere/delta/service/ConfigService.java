package dev.revere.delta.service;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.util.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
@Getter
public class ConfigService implements IService {

    private final Delta plugin;

    private final Map<String, File> configFiles = new HashMap<>();
    private final Map<String, FileConfiguration> fileConfigurations = new HashMap<>();

    private final String[] configFileNames = {
            "database/database.yml", "menus/grants-menu.yml", "menus/socials-menu.yml", "messages.yml", "settings.yml"
    };

    /**
     * Constructor for the ConfigService class
     *
     * @param plugin the main class of the plugin
     */
    public ConfigService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        for (String fileName : configFileNames) {
            loadConfig(fileName);
        }
    }

    public void reloadConfigs() {
        for (String fileName : configFileNames) {
            loadConfig(fileName);
        }
    }

    /**
     * Load a config
     *
     * @param fileName the name of the config
     */
    private void loadConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        this.configFiles.put(fileName, configFile);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        this.fileConfigurations.put(fileName, config);
    }

    /**
     * Save a config
     *
     * @param configFile        the config file to save
     * @param fileConfiguration the config to save
     */
    public void saveConfig(File configFile, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(configFile);
            fileConfiguration.load(configFile);
        } catch (Exception e) {
            Bukkit.broadcastMessage(CC.translate("&cError occurred while saving config: " + configFile.getName()));
        }
    }

    /**
     * Get a configuration file
     *
     * @param fileName the name of the file
     * @return the configuration file
     */
    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(Delta.getInstance().getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Get a config by its name
     *
     * @param fileName the name of the config
     * @return the config
     */
    public FileConfiguration getConfigByName(String fileName) {
        return this.fileConfigurations.get(fileName);
    }

    /**
     * Get a config file by its name
     *
     * @param fileName the name of the config
     * @return the config file
     */
    public File getConfigFileByName(String fileName) {
        return this.configFiles.get(fileName);
    }

    /**
     * Get formatted filenames of configuration files.
     *
     * @return formatted string of filenames
     */
    public String getConfigFileNames() {
        ConfigService configService = Delta.getInstance().getServiceManager().getService(ConfigService.class);
        Map<String, File> configFiles = configService.getConfigFiles();
        StringBuilder sb = new StringBuilder();

        for (String fileName : configFiles.keySet()) {
            sb.append(", ").append(fileName);
        }

        if (sb.length() > 2) {
            sb.delete(0, 2);
        }

        return sb.toString();
    }
}
