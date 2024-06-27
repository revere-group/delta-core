package dev.revere.delta.command.staff;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class SudoCommand extends BaseCommand {
    @Command(name = "sudo", permission = "delta.staff.sudo", usage = "/sudo <player> <command>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /sudo <player> <command>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        String commandBuilder = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        target.chat(CC.translate(commandBuilder));
        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.sudo"))
                .replace("%target%", target.getName())
                .replace("%command%", commandBuilder));
    }
}
