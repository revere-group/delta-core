package dev.revere.delta.command.player;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.cooldown.Cooldown;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.feature.cooldown.CooldownService;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Remi
 * @project Delta
 * @date 6/19/2024
 */
public class ReportCommand extends BaseCommand {
    @Command(name = "report", usage = "/report <player> <reason>", description = "Report a player")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /report <player> <reason>"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        if (player.equals(target)) {
            player.sendMessage(CC.translate("&cYou cannot report yourself."));
            return;
        }

        CooldownService cooldownService = Delta.getInstance().getServiceManager().getService(CooldownService.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), "REPORT_COMMAND"));

        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before reporting again."));
            return;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        player.sendMessage(CC.translate("&aYou have reported " + target.getName() + " for " + reason));

        FileConfiguration config = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml");

        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("delta.staff")).forEach(staff -> {
            staff.sendMessage(CC.translate(config.getString("staff.report")
                    .replace("%player%", player.getName())
                    .replace("%target%", target.getName())
                    .replace("%reason%", reason)));
        });

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(30 * 1000L, null);
            cooldownService.addCooldown(player.getUniqueId(), "REPORT_COMMAND", newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();
    }
}
