package dev.revere.delta.command.staff;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/21/2024
 */
public class VanishCommand extends BaseCommand {
    @Command(name = "vanish", aliases = {"v"}, permission = "delta.staff.vanish", description = "Toggle vanish", usage = "/vanish [player]")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());
            profile.getStaffOptions().setVanish(!profile.getStaffOptions().isVanish());
            player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.vanish.toggle"))
                    .replace("%status%", profile.getStaffOptions().isVanish() ? CC.translate("&aenabled") : CC.translate("&cdisabled"))
            );
            handleVisibility(player);
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(target.getUniqueId());
        profile.getStaffOptions().setVanish(!profile.getStaffOptions().isVanish());
        target.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.vanish.toggled-by"))
                .replace("%status%", profile.getStaffOptions().isVanish() ? CC.translate("&aenabled") : CC.translate("&cdisabled"))
                .replace("%player%", player.getName())
        );

        player.sendMessage(CC.translate(Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("staff.vanish.toggle-target"))
                .replace("%target%", target.getName())
                .replace("%status%", profile.getStaffOptions().isVanish() ? CC.translate("&aenabled") : CC.translate("&cdisabled"))
        );

        handleVisibility(target);
    }

    /**
     * Handle the visibility of the player
     *
     * @param player the player
     */
    private void handleVisibility(Player player) {
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        if (profile.getStaffOptions().isVanish()) {
            String quitMessage = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("on-leave.messages.left-the-game").replace("%player%", player.getName());
            player.getServer().getOnlinePlayers().forEach(online -> online.sendMessage(CC.translate(quitMessage)));
            player.setCanPickupItems(false);
            player.setCollidable(false);
            player.setInvulnerable(true);
            player.setAllowFlight(true);

            for (Player online : player.getServer().getOnlinePlayers()) {
                if (online.hasPermission("delta.staff.vanish")) {
                    online.showPlayer(player);
                } else {
                    online.hidePlayer(player);
                }
            }
        } else {
            String joinMessage = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("on-join.messages.joined-the-game").replace("%player%", player.getName());
            player.getServer().getOnlinePlayers().forEach(online -> online.sendMessage(CC.translate(joinMessage)));
            player.setCanPickupItems(true);
            player.setCollidable(true);
            player.setInvulnerable(false);
            player.setAllowFlight(false);
            for (Player online : player.getServer().getOnlinePlayers()) {
                online.showPlayer(player);
            }
        }
    }

}
