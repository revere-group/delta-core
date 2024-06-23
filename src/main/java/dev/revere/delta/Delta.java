package dev.revere.delta;

import dev.revere.delta.api.scoreboard.Assemble;
import dev.revere.delta.api.scoreboard.AssembleStyle;
import dev.revere.delta.api.service.ServiceManager;
import dev.revere.delta.command.CommandService;
import dev.revere.delta.database.MongoService;
import dev.revere.delta.feature.clan.ClanRepository;
import dev.revere.delta.feature.combat.CombatLogService;
import dev.revere.delta.feature.conversation.ConversationService;
import dev.revere.delta.feature.cooldown.CooldownService;
import dev.revere.delta.feature.home.HomeRepository;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.service.ListenerService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.ServerUtils;
import dev.revere.delta.visual.ScoreboardVisualizer;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Delta extends JavaPlugin {

    @Getter
    private static Delta instance;

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
        this.homeRepository = new HomeRepository();
        this.clanRepository = new ClanRepository();
    }

    private void registerServices() {
        serviceManager.registerService(new ConfigService(this));
        serviceManager.registerService(new MongoService(this));
        serviceManager.registerService(new ProfileService(this));
        serviceManager.registerService(new RankService(this));
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
}
