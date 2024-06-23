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
public class GMACommand extends BaseCommand {
    @Command(name = "gma", aliases = {"gm.a", "adventure", "gamemode.a", "gamemode.2", "gamemode.adventure", "gm.2", "gm2", "gm.adventure"}, permission = "delta.staff.gamemode.adventure", description = "Change your gamemode to adventure", usage = "/gma [player]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            if (player.getGameMode() == GameMode.ADVENTURE) {
                player.sendMessage(CC.translate("&cYou are already in adventure mode."));
                return;
            }

            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(CC.translate("&fYour gamemode has been updated to &badventure."));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        if (target.getGameMode() == GameMode.ADVENTURE) {
            player.sendMessage(CC.translate("&c" + target.getName() + " is already in adventure mode."));
            return;
        }

        target.setGameMode(GameMode.ADVENTURE);
        target.sendMessage(CC.translate("&fYour gamemode has been updated to &badventure &fby " + player.getName() + "&f."));
        player.sendMessage(CC.translate("&fYou have updated &b" + target.getName() + "'s &fgamemode to &badventure."));
    }
}
