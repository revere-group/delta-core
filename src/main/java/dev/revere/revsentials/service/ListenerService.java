package dev.revere.revsentials.service;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.service.Service;
import dev.revere.revsentials.feature.chat.listener.ChatListener;
import dev.revere.revsentials.feature.ore.OreListener;
import dev.revere.revsentials.profile.listener.ProfileListener;
import dev.revere.revsentials.profile.staff.listener.StaffListener;
import dev.revere.revsentials.util.menu.MenuListener;
import org.bukkit.Bukkit;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class ListenerService implements Service {

    private final Revsential plugin;

    /**
     * Constructor for the ListenerService class
     *
     * @param plugin the main class of the plugin
     */
    public ListenerService(Revsential plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        Bukkit.getPluginManager().registerEvents(new ProfileListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new StaffListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new OreListener(), plugin);
    }
}
