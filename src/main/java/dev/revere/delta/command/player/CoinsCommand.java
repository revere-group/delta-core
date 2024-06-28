package dev.revere.delta.command.player;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/27/2024
 */
public class CoinsCommand extends BaseCommand {
    @Command(name = "coins", aliases = {"money", "balance"}, description = "Check your coins balance", usage = "/coins")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        player.sendMessage(CC.translate(""));
        player.sendMessage(CC.translate("&b&lCoins &7Information:"));
        player.sendMessage(CC.translate(" &f● &fBalance: &b$" + profile.getCoins()));
        player.sendMessage(CC.translate(""));
        player.sendMessage(CC.translate("&7Do /shop to view the shop!"));
        player.sendMessage(CC.translate(""));
    }
}
