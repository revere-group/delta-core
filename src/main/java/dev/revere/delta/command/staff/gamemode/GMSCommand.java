package dev.revere.delta.command.staff.gamemode;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/19/2024
 */
public class GMSCommand extends BaseCommand {
    @Command(name = "gms", aliases = {"gm.s", "survival", "gamemode.s", "gamemode.0", "gamemode.survival", "gm.0", "gm0", "gm.survival"}, permission = "delta.staff.gamemode.survival", description = "Change your gamemode to survival", usage = "/gms [player]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            if (player.getGameMode() == GameMode.SURVIVAL) {
                player.sendMessage(CC.translate("&cYou are already in survival mode."));
                return;
            }

            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(CC.translate("&fYour gamemode has been updated to &bsurvival."));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        if (target.getGameMode() == GameMode.SURVIVAL) {
            player.sendMessage(CC.translate("&c" + target.getName() + " is already in survival mode."));
            return;
        }

        target.setGameMode(GameMode.SURVIVAL);
        target.sendMessage(CC.translate("&fYour gamemode has been updated to &bsurvival &fby " + player.getName() + "&f."));
        player.sendMessage(CC.translate("&fYou have updated &b" + target.getName() + "'s &fgamemode to &bsurvival."));
    }
}
