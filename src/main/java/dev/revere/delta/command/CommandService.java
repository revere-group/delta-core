package dev.revere.delta.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.CommandManager;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.command.player.DeltaCommand;
import dev.revere.delta.command.admin.*;
import dev.revere.delta.command.player.*;
import dev.revere.delta.command.player.ListCommand;
import dev.revere.delta.command.troll.*;
import dev.revere.delta.feature.clan.command.ClanCommand;
import dev.revere.delta.feature.clan.command.impl.*;
import dev.revere.delta.feature.combat.command.CombatLogCommand;
import dev.revere.delta.feature.combat.command.CombatLogResetCommand;
import dev.revere.delta.feature.combat.command.CombatLogStatusCommand;
import dev.revere.delta.feature.combat.command.CombatLogTimeCommand;
import dev.revere.delta.feature.conversation.command.impl.MessageCommand;
import dev.revere.delta.feature.conversation.command.impl.ReplyCommand;
import dev.revere.delta.command.staff.*;
import dev.revere.delta.command.staff.gamemode.GMACommand;
import dev.revere.delta.command.staff.gamemode.GMCCommand;
import dev.revere.delta.command.staff.gamemode.GMSCommand;
import dev.revere.delta.command.staff.gamemode.GMSPCommand;
import dev.revere.delta.feature.grant.command.GrantCommand;
import dev.revere.delta.feature.grant.command.SetRankCommand;
import dev.revere.delta.feature.home.command.HomeCommand;
import dev.revere.delta.feature.home.command.impl.*;
import dev.revere.delta.feature.rank.command.RankCommand;
import dev.revere.delta.feature.rank.command.impl.*;
import dev.revere.delta.feature.rank.menu.RankMenu;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class CommandService implements IService {

    private final Delta plugin;

    /**
     * Constructor for the CommandService class
     *
     * @param plugin the main class of the plugin
     */
    public CommandService(Delta plugin) {
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
        registerRankCommands();
    }

    private void registerPlayerCommands() {
        new DeltaCommand();
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

    private void registerRankCommands() {
        new RankRemovePermissionCommand();
        new RankAddPermissionCommand();
        new RankSetDefaultCommand();
        new RankSetWeightCommand();
        new RankSetPrefixCommand();
        new RankSetSuffixCommand();
        new RankSetStaffCommand();
        new RankCreateCommand();
        new RankDeleteCommand();
        new RankListCommand();
        new RankViewCommand();
        new RankMenuCommand();
        new RankCommand();

        new SetRankCommand();
        new GrantCommand();
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
        new StrikeCommand();
        new PushCommand();
        new LaunchCommand();
        new HurtCommand();
    }
}
