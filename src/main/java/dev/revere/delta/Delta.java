package dev.revere.delta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.revere.delta.api.service.ServiceManager;
import dev.revere.delta.command.CommandService;
import dev.revere.delta.database.MongoService;
import dev.revere.delta.feature.chat.filter.FilterService;
import dev.revere.delta.feature.conversation.ConversationService;
import dev.revere.delta.feature.cooldown.CooldownService;
import dev.revere.delta.feature.grant.GrantService;
import dev.revere.delta.feature.punishment.PunishmentService;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.feature.scoreboard.ScoreboardService;
import dev.revere.delta.feature.server.ServerService;
import dev.revere.delta.feature.server.whitelist.WhitelistService;
import dev.revere.delta.feature.tablist.TabListService;
import dev.revere.delta.feature.tag.TagService;
import dev.revere.delta.feature.tips.TipsService;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.service.ListenerService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.PlaceholderAPI;
import dev.revere.delta.util.ServerUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Delta extends JavaPlugin {

    @Getter
    private static Delta instance;

    @Getter
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Getter
    private final ServiceManager serviceManager = new ServiceManager();

    private final List<Runnable> initializationTasks = new ArrayList<>();

    private boolean isPlaceholderAPIEnabled;

    @Override
    public void onEnable() {
        instance = this;
        setupInitializationTasks();
        measureAndExecuteInitializationTasks();
    }

    @Override
    public void onDisable() {
        ServerUtils.disconnectPlayers();
    }

    /**
     * Register BungeeCord channel
     */
    private void registerBungeeChannel() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    /**
     * Hook additional plugins
     */
    private void hookAddons() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&fRegistered addon: &bPlaceholderAPI"));
            new PlaceholderAPI().register();
            isPlaceholderAPIEnabled = true;
        }
    }

    /**
     * Register services
     */
    private void registerServices() {
        serviceManager.registerService(new ConfigService(this));
        serviceManager.registerService(new ScoreboardService(this));
        serviceManager.registerService(new MongoService(this));
        serviceManager.registerService(new ProfileService(this));
        serviceManager.registerService(new RankService(this));
        serviceManager.registerService(new TagService(this));
        serviceManager.registerService(new GrantService(this));
        serviceManager.registerService(new FilterService(this));
        serviceManager.registerService(new ServerService(this));
        serviceManager.registerService(new WhitelistService(this));
        serviceManager.registerService(new CooldownService(this));
        serviceManager.registerService(new PunishmentService(this));
        serviceManager.registerService(new TipsService(this));
        serviceManager.registerService(new ListenerService(this));
        serviceManager.registerService(new TabListService(this));
        serviceManager.registerService(new CommandService(this));
        serviceManager.registerService(new ConversationService(this));
    }

    /**
     * Setup initialization tasks
     */
    private void setupInitializationTasks() {
        initializationTasks.add(this::registerServices);
        initializationTasks.add(serviceManager::registerAllServices);
        initializationTasks.add(this::registerBungeeChannel);
        initializationTasks.add(this::hookAddons);
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
