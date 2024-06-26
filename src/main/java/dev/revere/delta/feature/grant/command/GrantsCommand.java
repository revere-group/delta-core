package dev.revere.delta.feature.grant.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.grant.menu.grants.GrantsMenu;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 26/06/2024 - 22:37
 */
public class GrantsCommand extends BaseCommand {
    @Override
    @Command(name = "grants", permission = "delta.command.grants")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /grants (player)"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);

        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(targetPlayer.getUniqueId());
        if (profile == null) {
            player.sendMessage(CC.translate("&cThat player has never joined the server before."));
            return;
        }

        new GrantsMenu(true, targetPlayer).openMenu(player);
    }
}
