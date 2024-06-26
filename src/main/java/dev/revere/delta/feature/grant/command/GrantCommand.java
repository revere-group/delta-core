package dev.revere.delta.feature.grant.command;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.grant.menu.grant.GrantMenu;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 22:21
 */
public class GrantCommand extends BaseCommand {
    @Override
    @Command(name = "grant", permission = "delta.command.grant")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /grant (player)"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);

        Profile profile = dev.revere.delta.Delta.getInstance().getServiceManager().getService(dev.revere.delta.profile.ProfileService.class).getProfile(targetPlayer.getUniqueId());
        if (profile == null) {
            player.sendMessage(CC.translate("&cThat player has never joined the server before."));
            return;
        }

        new GrantMenu(targetPlayer).openMenu(player);
    }
}
