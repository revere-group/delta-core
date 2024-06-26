package dev.revere.delta.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandManager;
import dev.revere.delta.api.service.IService;
import org.bukkit.Bukkit;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

        registerCommandsInPackage("dev.revere.delta");
    }

    /**
     * Register all commands in a package
     *
     * @param packageName the package name
     */
    private void registerCommandsInPackage(String packageName) {
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
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                Bukkit.getLogger().severe("Failed to register command " + clazz.getSimpleName());
            }
        }
    }
}
