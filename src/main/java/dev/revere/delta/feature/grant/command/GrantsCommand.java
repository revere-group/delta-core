package dev.revere.delta.feature.grant.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.grant.menu.GrantMenu;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.DateUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 22:05
 */
public class GrantsCommand extends BaseCommand {
    @Override
    @Command(name = "grants", permission = "delta.command.grants", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /grants <player> [menu]"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer targetPlayer = player.getServer().getPlayer(targetName);

        if (args.length == 2 && args[1].equalsIgnoreCase("menu")) {
            new GrantMenu(true, targetPlayer).openMenu(player);
            return;
        }

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(targetPlayer.getUniqueId());

        List<Grant> activeGrants = profile.getGrants().stream().filter(Grant::isActive).toList();
        List<Grant> expiredGrants = profile.getGrants().stream().filter(grant -> !grant.isActive()).toList();

        player.sendMessage("");
        if (profile.getGrants().isEmpty()) {
            player.sendMessage(CC.translate("      &f● &cNo Grants available."));
        }

        if (!activeGrants.isEmpty()) {
            player.sendMessage(CC.translate("     &b&lActive Grant List &f(" + activeGrants.size() + "&f)"));
            activeGrants.forEach(grant -> player.sendMessage(CC.translate("      &f● &b" + grant.getRankName() + " &7(" + (grant.isPermanent() ? "Permanent" : DateUtils.formatTimeMillis(grant.getDuration() * 50L) + ", " + grant.getReason()) + "&7)")));
        }

        if (!expiredGrants.isEmpty()) {
            if (!activeGrants.isEmpty()) {
                player.sendMessage("");
            }
            player.sendMessage(CC.translate("     &b&lExpired Grant List &f(" + expiredGrants.size() + "&f)"));
            expiredGrants.forEach(grant -> player.sendMessage(CC.translate("      &f● &b" + grant.getRankName() + " &7(Removed by: " + grant.getRemovedBy() + "&7)")));
        }
        player.sendMessage("");
    }
}
