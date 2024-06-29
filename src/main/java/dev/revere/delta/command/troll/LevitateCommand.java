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
 * @date 29/06/2024 - 11:44
 */
public class LevitateCommand extends BaseCommand {
    @Override
    @Command(name = "levitate", aliases = {"float"}, permission = "delta.command.levitate", usage = "levitate (player) [value]", description = "Levitate a player")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /levitate (player)"));
            return;
        }

        String targetName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetName);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        if (targetPlayer.hasPotionEffect(PotionEffectType.LEVITATION)) {
            targetPlayer.removePotionEffect(PotionEffectType.LEVITATION);
            player.sendMessage(CC.translate("&b" + targetPlayer.getName() + " &7is no longer levitating."));
            targetPlayer.sendMessage(CC.translate("&7You are no longer levitating."));
            return;
        }

        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 1000000, 1));

        player.sendMessage(CC.translate("&b" + targetPlayer.getName() + " &7is now levitating."));
        targetPlayer.sendMessage(CC.translate("&7You are now levitating."));
    }
}
