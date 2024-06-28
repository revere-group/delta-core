package dev.revere.delta.feature.scoreboard;

import dev.revere.delta.Delta;
import dev.revere.delta.api.scoreboard.Assemble;
import dev.revere.delta.api.scoreboard.AssembleStyle;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.visual.ScoreboardVisualizer;

/**
 * @author Remi
 * @project Delta
 * @date 6/28/2024
 */
public class ScoreboardService implements IService {

    private final Delta plugin;

    /**
     * Constructor for the ScoreboardService class
     *
     * @param plugin the main class of the plugin
     */
    public ScoreboardService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        ConfigService configService = plugin.getServiceManager().getService(ConfigService.class);
        boolean scoreboardEnabled = configService.getConfig("settings.yml").getBoolean("scoreboard.enabled");
        if (!scoreboardEnabled) {
            return;
        }

        Assemble assemble = new Assemble(plugin, new ScoreboardVisualizer());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }
}
