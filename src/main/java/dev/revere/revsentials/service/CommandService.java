package dev.revere.revsentials.service;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.CommandManager;
import dev.revere.revsentials.api.service.Service;
import dev.revere.revsentials.command.RevsentialsCommand;
import dev.revere.revsentials.command.admin.*;
import dev.revere.revsentials.command.player.*;
import dev.revere.revsentials.command.staff.ClearInventoryCommand;
import dev.revere.revsentials.command.staff.HealCommand;
import dev.revere.revsentials.command.staff.SudoCommand;
import dev.revere.revsentials.command.troll.CaptureCommand;
import dev.revere.revsentials.feature.home.command.HomeCommand;
import dev.revere.revsentials.feature.home.command.impl.*;

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
        new RulesCommand();
        new PingCommand();
    }

    private void registerStaffCommands() {
        new ClearInventoryCommand();
        new SudoCommand();
        new HealCommand();
    }

    private void registerAdminCommands() {
        new InstanceCommand();
        new ClearLagCommand();
        new RebootCommand();
        new ReloadCommand();
        new AlertCommand();
    }

    private void registerHomeCommands() {
        new HomeTeleportCommand();
        new HomeDeleteCommand();
        new HomeListCommand();
        new HomeMenuCommand();
        new HomeSetCommand();
        new HomeCommand();
    }

    private void registerTrollCommands() {
        new CaptureCommand();
    }
}
