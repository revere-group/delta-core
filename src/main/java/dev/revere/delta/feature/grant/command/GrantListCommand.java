package dev.revere.delta.feature.grant.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 22:05
 */
public class GrantListCommand extends BaseCommand {
    @Override
    @Command(name = "grantlist", permission = "delta.command.grants", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sender.sendMessage(CC.translate("&cUsage: /grants <player>"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(targetPlayer.getUniqueId());
        if (profile == null) {
            sender.sendMessage(CC.translate("&cThat player has never joined the server before."));
            return;
        }

        List<Grant> activeGrants = profile.getGrants().stream().filter(Grant::isActive).toList();
        List<Grant> expiredGrants = profile.getGrants().stream().filter(grant -> !grant.isActive()).toList();

        sender.sendMessage("");
        if (profile.getGrants().isEmpty()) {
            sender.sendMessage(CC.translate("      &f● &cNo Grants available."));
        }

        if (!activeGrants.isEmpty()) {
            sender.sendMessage(CC.translate("     &b&lActive Grant List &f(" + activeGrants.size() + "&f)"));
            activeGrants.forEach(grant -> sender.sendMessage(CC.translate("      &f● &b" + grant.getRankName() + " &7(" + (grant.isPermanent() ? "Permanent" : DateUtils.formatTimeMillis(grant.getDuration() * 50L) + ", " + grant.getReason()) + "&7)")));
        }

        if (!expiredGrants.isEmpty()) {
            if (!activeGrants.isEmpty()) {
                sender.sendMessage("");
            }
            sender.sendMessage(CC.translate("     &b&lExpired Grant List &f(" + expiredGrants.size() + "&f)"));
            expiredGrants.forEach(grant -> sender.sendMessage(CC.translate("      &f● &b" + grant.getRankName() + " &7(Removed by: " + grant.getRemovedBy() + "&7)")));
        }
        sender.sendMessage("");
    }
}
