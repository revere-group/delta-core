package dev.revere.delta.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandManager;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.api.service.IService;
import org.bukkit.Bukkit;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

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

        Set<Class<? extends BaseCommand>> commandClasses = registerCommandsInPackage("dev.revere.delta");
        generateCommandDocumentation(commandClasses);
    }

    /**
     * Register all commands in a package.
     *
     * @param packageName the package name
     * @return Set of command classes found in the package.
     */
    private Set<Class<? extends BaseCommand>> registerCommandsInPackage(String packageName) {
        ConfigurationBuilder config = new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new SubTypesScanner(false))
                .filterInputsBy(new FilterBuilder().excludePackage("dev.revere.delta.api"));

        Reflections reflections = new Reflections(config);
        Set<Class<? extends BaseCommand>> commandClasses = reflections.getSubTypesOf(BaseCommand.class);
        for (Class<? extends BaseCommand> clazz : commandClasses) {
            try {
                Constructor<? extends BaseCommand> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                BaseCommand commandInstance = constructor.newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                     InvocationTargetException e) {
                Bukkit.getLogger().severe("Failed to register command " + clazz.getSimpleName());
            }
        }
        return commandClasses;
    }

    /**
     * Generate a Markdown document listing all commands and their permissions.
     *
     * @param commandClasses Set of command classes to document.
     */
    private void generateCommandDocumentation(Set<Class<? extends BaseCommand>> commandClasses) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File documentationFile = new File(dataFolder, "commands_documentation.md");
        try (FileWriter writer = new FileWriter(documentationFile)) {
            writer.write("# Commands Documentation\n\n");
            for (Class<? extends BaseCommand> clazz : commandClasses) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Command.class)) {
                        Command command = method.getAnnotation(Command.class);
                        writer.write("## " + command.name() + "\n");
                        writer.write("- **Permission:** " + command.permission() + "\n");
                        writer.write("- **Description:** " + command.description() + "\n");
                        writer.write("- **Usage:** " + command.usage() + "\n\n");
                    }
                }
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to generate command documentation: " + e.getMessage());
        }
    }
}