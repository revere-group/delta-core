package dev.revere.revsentials.service;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.service.Service;
import dev.revere.revsentials.util.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
@Getter
public class ConfigService implements Service {

    private final Revsential plugin;

    private final Map<String, File> configFiles = new HashMap<>();
    private final Map<String, FileConfiguration> fileConfigurations = new HashMap<>();

    private final String[] configFileNames = {
            "messages.yml", "settings.yml", "storage/homes.yml", "menus/socials-menu.yml"
    };

    /**
     * Constructor for the ConfigService class
     *
     * @param plugin the main class of the plugin
     */
    public ConfigService(Revsential plugin) {
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
}
