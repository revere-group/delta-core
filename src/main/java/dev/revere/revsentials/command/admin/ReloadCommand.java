package dev.revere.revsentials.command.admin;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.service.ConfigService;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class ReloadCommand extends BaseCommand {
    @Command(name = "revsentials.reload", permission = "revsentials.admin.reload", description = "Reload command for Revsentials")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        long start = System.currentTimeMillis();

        for (String message : Revsential.getInstance().getConfig("messages.yml").getStringList("reload.reloading")) {
            player.sendMessage(CC.translate(message));
        }

        Revsential.getInstance().getServiceManager().getService(ConfigService.class).reloadConfigs();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        for (String message : Revsential.getInstance().getConfig("messages.yml").getStringList("reload.finished")) {
            player.sendMessage(CC.translate(message.replace("%timetaken%", String.valueOf(timeTaken))));
        }
    }
}
