package dev.revere.delta.command.staff;

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
public class SudoAllCommand extends BaseCommand {
    @Command(name = "sudoall", permission = "delta.staff.sudoall", usage = "/sudoall <command>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /sudoall <command>"));
            return;
        }

        String commandBuilder = String.join(" ", args);

        for (Player target : Delta.getInstance().getServer().getOnlinePlayers()) {
            target.chat(CC.translate(commandBuilder));
        }

        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.sudo-all"))
                .replace("%command%", commandBuilder));
    }
}
