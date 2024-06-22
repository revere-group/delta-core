package dev.revere.revsentials;

import dev.revere.revsentials.api.service.ServiceManager;
import dev.revere.revsentials.command.CommandService;
import dev.revere.revsentials.feature.clan.ClanRepository;
import dev.revere.revsentials.feature.combat.CombatLogService;
import dev.revere.revsentials.feature.conversation.ConversationService;
import dev.revere.revsentials.feature.cooldown.CooldownService;
import dev.revere.revsentials.feature.home.HomeRepository;
import dev.revere.revsentials.profile.ProfileService;
import dev.revere.revsentials.service.*;
import dev.revere.revsentials.util.CC;
import dev.revere.revsentials.util.ServerUtils;
import dev.revere.revsentials.api.scoreboard.Assemble;
import dev.revere.revsentials.api.scoreboard.AssembleStyle;
import dev.revere.revsentials.visual.ScoreboardVisualizer;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Revsential extends JavaPlugin {

    @Getter
    private static Revsential instance;

    @Getter
    private final ServiceManager serviceManager = new ServiceManager();

    private final List<Runnable> initializationTasks = new ArrayList<>();

    private HomeRepository homeRepository;
    private ClanRepository clanRepository;

    @Override
    public void onEnable() {
        instance = this;
        registerScoreboard();
        setupInitializationTasks();
        measureAndExecuteInitializationTasks();
    }

    @Override
    public void onDisable() {
        ServerUtils.disconnectPlayers();
    }

    private void registerScoreboard() {
        Assemble assemble = new Assemble(this, new ScoreboardVisualizer());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }

    private void registerRepositories() {
        homeRepository = new HomeRepository();
        clanRepository = new ClanRepository();
    }

    private void registerServices() {
        serviceManager.registerService(new ConfigService(this));
        serviceManager.registerService(new ProfileService(this));
        serviceManager.registerService(new CooldownService(this));
        serviceManager.registerService(new ListenerService(this));
        serviceManager.registerService(new CommandService(this));
        serviceManager.registerService(new CombatLogService(this));
        serviceManager.registerService(new ConversationService(this));
    }

    /**
     * Setup initialization tasks
     */
    private void setupInitializationTasks() {
        initializationTasks.add(this::registerServices);
        initializationTasks.add(serviceManager::registerAllServices);
        initializationTasks.add(this::registerRepositories);
    }

    /**
     * Measure and execute initialization tasks
     */
    private void measureAndExecuteInitializationTasks() {
        long start = System.currentTimeMillis();

        for (Runnable task : initializationTasks) {
            task.run();
        }

        long end = System.currentTimeMillis();
        long time = end - start;
        CC.enableMessage(time);
    }

    /**
     * Get a configuration file
     *
     * @param fileName the name of the file
     * @return the configuration file
     */
    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
