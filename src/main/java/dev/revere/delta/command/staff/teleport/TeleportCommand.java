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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 27/06/2024 - 19:31
 */
public class TeleportCommand extends BaseCommand {
    @Override
    @Command(name = "teleport", permission = "delta.staff.teleport", aliases = {"tp", "tpto", "teleportto"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /teleport (player)"));
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

        player.teleport(targetPlayer.getLocation());
        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.teleport.teleported"))
                .replace("%target-rank-color%", String.valueOf(Delta.getInstance().getServiceManager().getService(RankService.class).getHighestRank(targetProfile).getNameColor()))
                .replace("%target%", targetPlayer.getName()));
    }
}
