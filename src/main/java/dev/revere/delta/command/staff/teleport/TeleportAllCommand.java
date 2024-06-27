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
 * @date 27/06/2024 - 19:34
 */
public class TeleportAllCommand extends BaseCommand {
    @Override
    @Command(name = "teleportall", permission = "delta.staff.tpall", aliases = {"tp-all", "tpall", "tpeverybody"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Bukkit.getOnlinePlayers().size() == 1) {
            player.sendMessage(CC.translate("&cThere are no other players online to teleport."));
            return;
        }

        Profile playerProfile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) onlinePlayers.teleport(player.getLocation());
        Bukkit.broadcastMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.teleport.tp-all"))
                .replace("%rank-color%", String.valueOf(Delta.getInstance().getServiceManager().getService(RankService.class).getHighestRank(playerProfile).getNameColor()))
                .replace("%player%", player.getName()));
    }
}
