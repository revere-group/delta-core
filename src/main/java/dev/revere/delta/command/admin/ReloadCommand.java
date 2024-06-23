package dev.revere.delta.command.admin;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class ReloadCommand extends BaseCommand {
    @Command(name = "delta.reload", permission = "delta.admin.reload", description = "Reload command for Revsentials")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        long start = System.currentTimeMillis();

        for (String message : Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getStringList("reload.reloading")) {
            player.sendMessage(CC.translate(message));
        }

        Delta.getInstance().getServiceManager().getService(ConfigService.class).reloadConfigs();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        for (String message : Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getStringList("reload.finished")) {
            player.sendMessage(CC.translate(message.replace("%timetaken%", String.valueOf(timeTaken))));
        }
    }
}
