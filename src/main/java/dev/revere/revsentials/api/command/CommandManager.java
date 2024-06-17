package dev.revere.revsentials.api.command;

import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class CommandManager implements CommandExecutor {

    private final Map<String, Entry<Method, Object>> commandMap = new HashMap<>();
    private CommandMap commandMapInstance;
    private final JavaPlugin plugin;
    public static CommandManager instance;

    public static CommandManager getInstance() {
        return instance;
    }

    public CommandManager(JavaPlugin plugin) {
        instance = this;
        this.plugin = plugin;
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            this.commandMapInstance = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        return handleCommand(sender, cmd, label, args);
    }

    public boolean handleCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        for (int i = args.length; i >= 0; i--) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append(".").append(args[x].toLowerCase());
            }
            String cmdLabel = buffer.toString();
            if (commandMap.containsKey(cmdLabel)) {
                Method method = commandMap.get(cmdLabel).getKey();
                Object methodObject = commandMap.get(cmdLabel).getValue();
                Command command = method.getAnnotation(Command.class);

                if (!command.permission().isEmpty() && (!sender.hasPermission(command.permission()))) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return true;
                }
                if (command.inGameOnly() && !(sender instanceof Player)) {
                    sender.sendMessage(CC.translate("&cThis command in only executable in game."));
                    return true;
                }

                try {
                    method.invoke(methodObject,
                            new CommandArgs(sender, cmd, label, args, cmdLabel.split("\\.").length - 1));
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return true;
    }

    public void registerCommands(Object obj, List<String> aliases) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(Command.class) != null) {
                Command command = m.getAnnotation(Command.class);
                if (m.getParameterTypes().length > 1 || m.getParameterTypes()[0] != CommandArgs.class) {
                    System.out.println("Unable to register command " + m.getName() + ". Unexpected method arguments");
                    continue;
                }
                registerCommand(command, command.name(), m, obj);

                for (String alias : command.aliases()) {
                    registerCommand(command, alias, m, obj);
                }
                if (aliases != null) {
                    for (String alias : aliases) {
                        registerCommand(command, alias, m, obj);
                    }
                }
            }
        }
    }

    public void registerHelp() {
        Set<HelpTopic> help = new TreeSet<>(HelpTopicComparator.helpTopicComparatorInstance());
        for (String s : commandMap.keySet()) {
            if (!s.contains(".")) {
                org.bukkit.command.Command cmd = commandMapInstance.getCommand(s);
                HelpTopic topic = new GenericCommandHelpTopic(cmd);
                help.add(topic);
            }
        }
        IndexHelpTopic topic = new IndexHelpTopic(plugin.getName(), "All commands for " + plugin.getName(), null, help,
                "Below is a list of all " + plugin.getName() + " commands:");
        Bukkit.getServer().getHelpMap().addTopic(topic);
    }

    public void unregisterCommands(Object obj) {
        for (Method m : obj.getClass().getMethods()) {
            if (m.getAnnotation(Command.class) != null) {
                Command command = m.getAnnotation(Command.class);
                commandMap.remove(command.name().toLowerCase());
                commandMap.remove(this.plugin.getName() + ":" + command.name().toLowerCase());
                Objects.requireNonNull(commandMapInstance.getCommand(command.name().toLowerCase())).unregister(commandMapInstance);
            }
        }
    }

    public void registerCommand(Command command, String label, Method m, Object obj) {
        commandMap.put(label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        commandMap.put(this.plugin.getName() + ':' + label.toLowerCase(), new AbstractMap.SimpleEntry<>(m, obj));
        String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();

        if (commandMapInstance.getCommand(cmdLabel) == null) {
            org.bukkit.command.Command cmd = new BukkitCommand(cmdLabel, this, plugin);
            commandMapInstance.register(plugin.getName(), cmd);
        }

        if (!command.description().equalsIgnoreCase("") && cmdLabel == label) {
            commandMapInstance.getCommand(cmdLabel).setDescription(command.description());
        }

        if (!command.usage().equalsIgnoreCase("") && cmdLabel == label) {
            commandMapInstance.getCommand(cmdLabel).setUsage(command.usage());
        }
    }
}