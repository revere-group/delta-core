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
public class GMSPCommand extends BaseCommand {
    @Command(name = "gmsp", aliases = {"gm.sp", "spectator", "gamemode.sp", "gamemode.3", "gamemode.spectator", "gm.3", "gm3", "gm.spectator"}, permission = "delta.staff.gamemode.spectator", description = "Change your gamemode to spectator", usage = "/gmsp [player]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                player.sendMessage(CC.translate("&cYou are already in spectator mode."));
                return;
            }

            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(CC.translate("&fYour gamemode has been updated to &bspectator."));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        if (target.getGameMode() == GameMode.SPECTATOR) {
            player.sendMessage(CC.translate("&c" + target.getName() + " is already in spectator mode."));
            return;
        }

        target.setGameMode(GameMode.SPECTATOR);
        target.sendMessage(CC.translate("&fYour gamemode has been updated to &bspectator &fby " + player.getName() + "&f."));
        player.sendMessage(CC.translate("&fYou have updated &b" + target.getName() + "'s &fgamemode to &bspectator."));
    }
}
