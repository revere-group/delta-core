package dev.revere.delta.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class BukkitCommand extends Command {

    private final Plugin ownerPlugin;
    private final CommandExecutor executor;

    /**
     * Creates a new command with the given name and description.
     *
     * @param label    the command's name
     * @param executor the command's executor
     * @param owner    the plugin that owns the command
     */
    protected BukkitCommand(String label, CommandExecutor executor, Plugin owner) {
        super(label);
        this.executor = executor;
        this.ownerPlugin = owner;
        this.usageMessage = "";
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean success;

        if (!ownerPlugin.isEnabled()) return false;
        if (!testPermission(sender)) return true;

        try {
            success = executor.onCommand(sender, this, commandLabel, args);
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + ownerPlugin.getDescription().getFullName(), ex);
        }

        if (!success && !usageMessage.isEmpty()) {
            Arrays.stream(usageMessage.replace("<command>", commandLabel).split("\n")).forEach(sender::sendMessage);
        }

        return success;
    }
}
