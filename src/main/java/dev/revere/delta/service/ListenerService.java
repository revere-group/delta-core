package dev.revere.delta.service;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.feature.chat.listener.ChatListener;
import dev.revere.delta.feature.staff.listener.StaffListener;
import dev.revere.delta.util.menu.MenuListener;
import org.bukkit.Bukkit;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class ListenerService implements IService {

    private final Delta plugin;

    /**
     * Constructor for the ListenerService class
     *
     * @param plugin the main class of the plugin
     */
    public ListenerService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        Bukkit.getPluginManager().registerEvents(new StaffListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
    }
}
