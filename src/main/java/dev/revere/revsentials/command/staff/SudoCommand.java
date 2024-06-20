package dev.revere.revsentials.command.staff;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.raid.RaidEvent;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class SudoCommand extends BaseCommand {
    @Command(name = "sudo", permission = "revsentials.staff.sudo", usage = "/sudo <player> <command>")
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

        String commandBuilder = IntStream.range(1, args.length).mapToObj(i -> args[i] + " ").collect(Collectors.joining());

        target.chat(CC.translate(commandBuilder));
        player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("staff.sudo.format"))
                .replace("%target%", target.getName())
                .replace("%command%", commandBuilder));
    }
}
