package dev.revere.delta.command.staff.coins.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/27/2024
 */
public class CoinsTakeCommand extends BaseCommand {
    @Command(name = "coinsmanager.take", aliases = {"cmanager.take", "coinsm.take", "cm.take"}, permission = "delta.staff.coins.take")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /coinsmanager take <player> <amount>"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer target = player.getServer().getOfflinePlayer(targetName);

        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(target.getUniqueId());
        if (profile == null) {
            player.sendMessage(CC.translate("&cThat player has never joined the server."));
            return;
        }

        profile.setCoins(profile.getCoins() < Integer.parseInt(args[1]) ? 0 : profile.getCoins() - Integer.parseInt(args[1]));

        player.sendMessage(CC.translate("&fYou have taken &b" + args[1] + " &fcoins from &b" + target.getName() + "&f."));

        if (target.isOnline()) {
            target.getPlayer().sendMessage(CC.translate("&c" + player.getName() + " has taken " + args[1] + " coins from you."));
        }
    }
}
