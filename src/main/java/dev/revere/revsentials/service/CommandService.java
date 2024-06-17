package dev.revere.revsentials.service;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.CommandManager;
import dev.revere.revsentials.api.service.Service;
import dev.revere.revsentials.command.RevsentialsCommand;
import dev.revere.revsentials.command.admin.AlertCommand;
import dev.revere.revsentials.command.admin.InstanceCommand;
import dev.revere.revsentials.command.admin.RebootCommand;
import dev.revere.revsentials.command.admin.ReloadCommand;
import dev.revere.revsentials.command.player.LocationCommand;
import dev.revere.revsentials.command.player.PingCommand;
import dev.revere.revsentials.command.player.PlayTimeCommand;
import dev.revere.revsentials.command.player.SocialCommand;
import dev.revere.revsentials.command.staff.ClearInventoryCommand;
import dev.revere.revsentials.command.staff.HealCommand;
import dev.revere.revsentials.command.staff.SudoCommand;
import dev.revere.revsentials.command.troll.CaptureCommand;
import dev.revere.revsentials.feature.home.command.HomeCommand;
import dev.revere.revsentials.feature.home.command.impl.HomeDeleteCommand;
import dev.revere.revsentials.feature.home.command.impl.HomeListCommand;
import dev.revere.revsentials.feature.home.command.impl.HomeSetCommand;
import dev.revere.revsentials.feature.home.command.impl.HomeTeleportCommand;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class CommandService implements Service {

    private final Revsential plugin;

    /**
     * Constructor for the CommandService class
     *
     * @param plugin the main class of the plugin
     */
    public CommandService(Revsential plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        new CommandManager(plugin);
        registerPlayerCommands();
        registerTrollCommands();
        registerStaffCommands();
        registerAdminCommands();
        registerHomeCommands();
    }

    private void registerPlayerCommands() {
        new RevsentialsCommand();
        new LocationCommand();
        new PlayTimeCommand();
        new SocialCommand();
        new PingCommand();
    }

    private void registerStaffCommands() {
        new ClearInventoryCommand();
        new SudoCommand();
        new HealCommand();
    }

    private void registerAdminCommands() {
        new InstanceCommand();
        new RebootCommand();
        new ReloadCommand();
        new AlertCommand();
    }

    private void registerHomeCommands() {
        new HomeSetCommand();
        new HomeDeleteCommand();
        new HomeTeleportCommand();
        new HomeListCommand();
        new HomeCommand();
    }

    private void registerTrollCommands() {
        new CaptureCommand();
    }
}
