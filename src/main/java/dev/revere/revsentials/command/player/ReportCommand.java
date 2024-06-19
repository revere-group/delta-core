package dev.revere.revsentials.command.player;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.cooldown.Cooldown;
import dev.revere.revsentials.service.ConfigService;
import dev.revere.revsentials.service.CooldownService;
import dev.revere.revsentials.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Remi
 * @project Revsentials
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

        CooldownService cooldownService = Revsential.getInstance().getServiceManager().getService(CooldownService.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), "REPORT_COMMAND"));

        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before reporting again."));
            return;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        player.sendMessage(CC.translate("&aYou have reported " + target.getName() + " for " + reason));

        FileConfiguration config = Revsential.getInstance().getServiceManager().getService(ConfigService.class).getConfigByName("messages.yml");

        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("revsentials.staff")).forEach(staff -> {
            staff.sendMessage(CC.translate(config.getString("staff.report.format")
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
