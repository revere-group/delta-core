package dev.revere.revsentials.command;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.CommandManager;
import dev.revere.revsentials.api.service.Service;
import dev.revere.revsentials.command.player.RevsentialsCommand;
import dev.revere.revsentials.command.admin.*;
import dev.revere.revsentials.command.player.*;
import dev.revere.revsentials.command.player.ListCommand;
import dev.revere.revsentials.feature.clan.command.ClanCommand;
import dev.revere.revsentials.feature.clan.command.impl.*;
import dev.revere.revsentials.feature.combat.command.CombatLogCommand;
import dev.revere.revsentials.feature.combat.command.CombatLogResetCommand;
import dev.revere.revsentials.feature.combat.command.CombatLogStatusCommand;
import dev.revere.revsentials.feature.combat.command.CombatLogTimeCommand;
import dev.revere.revsentials.feature.conversation.command.impl.MessageCommand;
import dev.revere.revsentials.feature.conversation.command.impl.ReplyCommand;
import dev.revere.revsentials.command.staff.*;
import dev.revere.revsentials.command.staff.gamemode.GMACommand;
import dev.revere.revsentials.command.staff.gamemode.GMCCommand;
import dev.revere.revsentials.command.staff.gamemode.GMSCommand;
import dev.revere.revsentials.command.staff.gamemode.GMSPCommand;
import dev.revere.revsentials.command.troll.CaptureCommand;
import dev.revere.revsentials.command.troll.LaunchCommand;
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
        registerCombatCommands();
        registerTrollCommands();
        registerStaffCommands();
        registerAdminCommands();
        registerHomeCommands();
        registerClanCommands();
    }

    private void registerPlayerCommands() {
        new RevsentialsCommand();
        new LocationCommand();
        new PlayTimeCommand();
        new VersionCommand();
        new MessageCommand();
        new ReportCommand();
        new HelpopCommand();
        new SocialCommand();
        new ReplyCommand();
        new RulesCommand();
        new ListCommand();
        new PingCommand();
    }

    private void registerStaffCommands() {
        new SeeEnderInventoryCommand();
        new ClearInventoryCommand();
        new StaffChatCommand();
        new InvSeeCommand();
        new VanishCommand();
        new MoreCommand();
        new SudoCommand();
        new HealCommand();
        new GMSPCommand();
        new GMACommand();
        new GMSCommand();
        new GMCCommand();
        new DayCommand();
        new FlyCommand();
    }

    private void registerAdminCommands() {
        new BroadcastCommand();
        new InstanceCommand();
        new ClearLagCommand();
        new RebootCommand();
        new ReloadCommand();
        new AlertCommand();
    }

    private void registerCombatCommands() {
        new CombatLogStatusCommand();
        new CombatLogResetCommand();
        new CombatLogTimeCommand();
        new CombatLogCommand();
    }

    private void registerClanCommands() {
        new ClanSetColorCommand();
        new ClanSetHomeCommand();
        new ClanCreateCommand();
        new ClanDeleteCommand();
        new ClanInviteCommand();
        new ClanLeaveCommand();
        new ClanHomeCommand();
        new ClanJoinCommand();
        new ClanInfoCommand();
        new ClanKickCommand();
        new ClanListCommand();
        new ClanChatCommand();
        new ClanCommand();
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
        new LaunchCommand();
    }
}
