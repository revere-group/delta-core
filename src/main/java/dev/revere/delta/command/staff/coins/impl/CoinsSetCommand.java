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
public class CoinsSetCommand extends BaseCommand {
    @Command(name = "coinsmanager.set", aliases = {"cmanager.set", "coinsm.set", "cm.set"}, permission = "delta.staff.coins.set")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /coinsmanager set <player> <amount>"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer target = player.getServer().getOfflinePlayer(targetName);

        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(target.getUniqueId());
        if (profile == null) {
            player.sendMessage(CC.translate("&cThat player has never joined the server."));
            return;
        }

        profile.setCoins(Integer.parseInt(args[1]));

        player.sendMessage(CC.translate("&fYou have set &b" + target.getName() + "'s &fcoins to &b" + args[1] + "&f."));

        if (target.isOnline()) {
            target.getPlayer().sendMessage(CC.translate("&a" + player.getName() + " has set your coins to " + args[1] + "."));
        }
    }
}
