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
public class CoinsViewCommand extends BaseCommand {
    @Command(name = "coinsmanager.view", aliases = {"cmanager.view", "coinsm.view", "cm.view"}, permission = "delta.staff.coins.view")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /coinsmanager view <player>"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer target = player.getServer().getOfflinePlayer(targetName);

        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(target.getUniqueId());
        if (profile == null) {
            player.sendMessage(CC.translate("&cThat player has never joined the server."));
            return;
        }

        player.sendMessage(CC.translate(""));
        player.sendMessage(CC.translate("&b&l " + targetName + "'s &7Coins Information:"));
        player.sendMessage(CC.translate(" &f● &fBalance: &b$" + profile.getCoins()));
        player.sendMessage(CC.translate(""));
    }
}
