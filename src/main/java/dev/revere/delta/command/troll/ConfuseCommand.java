package dev.revere.delta.command.troll;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 07:58
 */
public class ConfuseCommand extends BaseCommand {
    @Override
    @Command(name = "confuse", aliases = "nausea", permission = "delta.command.confuse")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /confuse (player)"));
            return;
        }

        Player targetPlayer = player.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&fNo player matching &b" + args[0] + " &fis connected to this server."));
            return;
        }

        if (targetPlayer.hasPotionEffect(PotionEffectType.NAUSEA)) {
            targetPlayer.removePotionEffect(PotionEffectType.NAUSEA);
            player.sendMessage(CC.translate("&fYou've removed &b" + targetPlayer.getName() + "'s &fnausea."));
            targetPlayer.sendMessage(CC.translate("&aYou're no longer confused."));
            return;
        }

        targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 1000000, 1));
        player.sendMessage(CC.translate("&fYou've confused &b" + targetPlayer.getName()));
        targetPlayer.sendMessage(CC.translate("&aYou're now confused."));
    }
}
