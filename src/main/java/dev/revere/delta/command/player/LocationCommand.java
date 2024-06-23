package dev.revere.delta.command.player;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.cooldown.Cooldown;
import dev.revere.delta.feature.cooldown.CooldownService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class LocationCommand extends BaseCommand {
    @Command(name = "location", aliases = {"loc"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&fYour location is: &b" + x + ", " + y + ", " + z));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        CooldownService cooldownService = Delta.getInstance().getServiceManager().getService(CooldownService.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), "LOCATION_COMMAND"));

        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before using this command again."));
            return;
        }

        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("player.location.shared"))
                .replace("%target%", target.getName()));

        target.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("player.location.received"))
                .replace("%player%", player.getName())
                .replace("%x%", String.valueOf(x))
                .replace("%y%", String.valueOf(y))
                .replace("%z%", String.valueOf(z))
        );

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(15 * 1000L, null);
            cooldownService.addCooldown(player.getUniqueId(), "LOCATION_COMMAND", newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();
    }
}
