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
public class GMCCommand extends BaseCommand {
    @Command(name = "gmc", aliases = {"gm.c", "creative", "gamemode.c", "gamemode.1", "gamemode.creative", "gm.1", "gm1", "gm.creative"}, permission = "delta.staff.gamemode.creative", description = "Change your gamemode to creative", usage = "/gmc [player]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            if (player.getGameMode() == GameMode.CREATIVE) {
                player.sendMessage(CC.translate("&cYou are already in creative mode."));
                return;
            }

            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(CC.translate("&fYour gamemode has been updated to &bcreative."));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        if (target.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage(CC.translate("&c" + target.getName() + " is already in creative mode."));
            return;
        }

        target.setGameMode(GameMode.CREATIVE);
        target.sendMessage(CC.translate("&fYour gamemode has been updated to &bcreative &fby " + player.getName() + "&f."));
        player.sendMessage(CC.translate("&fYou have updated &b" + target.getName() + "'s &fgamemode to &bcreative."));
    }
}
