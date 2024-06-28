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
public class CoinsResetCommand extends BaseCommand {
    @Command(name = "coinsmanager.reset", aliases = {"cmanager.reset", "coinsm.reset", "cm.reset"}, permission = "delta.staff.coins.reset")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /coinsmanager reset <player>"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer target = player.getServer().getOfflinePlayer(targetName);

        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(target.getUniqueId());
        if (profile == null) {
            player.sendMessage(CC.translate("&cThat player has never joined the server."));
            return;
        }

        profile.setCoins(0);

        player.sendMessage(CC.translate("&fYou have reset &b" + target.getName() + "'s &fcoins."));

        if (target.isOnline()) {
            target.getPlayer().sendMessage(CC.translate("&c" + player.getName() + " has reset your coins."));
        }
    }
}
