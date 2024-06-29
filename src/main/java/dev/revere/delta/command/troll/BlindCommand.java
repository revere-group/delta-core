package dev.revere.delta.command.troll;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 11:48
 */
public class BlindCommand extends BaseCommand {
    @Override
    @Command(name = "blind", permission = "delta.command.blind", usage = "blind (player)", description = "Blind a player")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /blind (player)"));
            return;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        if (targetPlayer.hasPotionEffect(PotionEffectType.BLINDNESS)) {
            targetPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
            player.sendMessage(CC.translate("&b" + targetPlayer.getName() + " &7is no longer blind."));
            targetPlayer.sendMessage(CC.translate("&7You are no longer blind."));
            return;
        }

        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000, 1));
        player.sendMessage(CC.translate("&b" + targetPlayer.getName() + " &7is now blind."));
        targetPlayer.sendMessage(CC.translate("&7You are now blind."));
    }
}
