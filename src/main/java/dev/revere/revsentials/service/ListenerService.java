package dev.revere.revsentials.service;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.CommandManager;
import dev.revere.revsentials.api.service.Service;
import dev.revere.revsentials.command.admin.AlertCommand;
import dev.revere.revsentials.command.admin.RebootCommand;
import dev.revere.revsentials.command.player.LocationCommand;
import dev.revere.revsentials.command.player.PingCommand;
import dev.revere.revsentials.command.staff.ClearInventoryCommand;
import dev.revere.revsentials.command.staff.HealCommand;
import dev.revere.revsentials.command.staff.SudoCommand;
import dev.revere.revsentials.feature.ore.OreListener;
import dev.revere.revsentials.feature.staff.StaffListener;
import dev.revere.revsentials.profile.listener.ProfileListener;
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
        Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new OreListener(), plugin);
    }
}
