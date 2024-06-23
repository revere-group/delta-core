package dev.revere.delta.command.player;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.cooldown.Cooldown;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.feature.cooldown.CooldownService;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Remi
 * @project Delta
 * @date 6/19/2024
 */
public class HelpopCommand extends BaseCommand {
    @Command(name = "helpop", aliases = {"help", "request"}, description = "Request help from staff")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /helpop <reason>"));
            return;
        }

        CooldownService cooldownService = Delta.getInstance().getServiceManager().getService(CooldownService.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), "HELP_COMMAND"));

        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before requesting help again."));
            return;
        }

        String reason = String.join(" ", args);
        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("player.help-request-sent")
                .replace("%reason%", reason)
        ));

        FileConfiguration config = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml");

        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("delta.staff")).forEach(staff -> {
            staff.sendMessage(CC.translate(config.getString("staff.help.format"))
                    .replace("%player%", player.getName())
                    .replace("%message%", reason));
        });

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(30 * 1000L, null);
            cooldownService.addCooldown(player.getUniqueId(), "HELP_COMMAND", newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();
    }
}
