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

        CooldownService cooldownService = Revsential.getInstance().getServiceManager().getService(CooldownService.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), "HELP_COMMAND"));

        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before requesting help again."));
            return;
        }

        String reason = String.join(" ", args);
        player.sendMessage(CC.translate(Revsential.getInstance().getConfig("messages.yml").getString("player.help-request-sent")
                .replace("%reason%", reason)
        ));

        FileConfiguration config = Revsential.getInstance().getServiceManager().getService(ConfigService.class).getConfigByName("messages.yml");

        Bukkit.getOnlinePlayers().stream().filter(staff -> staff.hasPermission("revsentials.staff")).forEach(staff -> {
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
