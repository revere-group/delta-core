package dev.revere.delta.command.staff.teleport;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 27/06/2024 - 19:38
 */
public class TeleportHereCommand extends BaseCommand {

    @Override
    @Command(name = "teleporthere", permission = "delta.staff.tphere", aliases = {"tphere", "s"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /tphere (player)"));
            return;
        }

        Player targetPlayer = player.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&fNo player matching &b" + args[0] + " &fis connected to this server."));
            return;
        }

        if (targetPlayer == player) {
            player.sendMessage(CC.translate("&cYou cannot teleport yourself to yourself."));
            return;
        }

        Profile targetProfile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(targetPlayer.getUniqueId());
        Profile playerProfile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.teleport.tp-here.sender")
                .replace("%rank-color%", String.valueOf(Delta.getInstance().getServiceManager().getService(RankService.class).getHighestRank(targetProfile).getNameColor()))
                .replace("%target%", targetPlayer.getDisplayName())));
        targetPlayer.teleport(player.getLocation());
        targetPlayer.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.teleport.tp-here.target")
                .replace("%rank-color%", String.valueOf(Delta.getInstance().getServiceManager().getService(RankService.class).getHighestRank(playerProfile).getNameColor()))
                .replace("%player%", player.getName())));
    }
}
