package dev.revere.delta.profile.listener;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.punishment.Punishment;
import dev.revere.delta.feature.punishment.PunishmentService;
import dev.revere.delta.feature.punishment.PunishmentType;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.DateUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class ProfileListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        Profile profile = new Profile(player.getUniqueId());
        profile.loadProfile();

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        profileService.addProfile(profile.getUuid(), profile);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void handlePunishmentLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        if (profile == null) {
            return;
        }

        if (profile.getPunishments().stream().anyMatch(punishment -> punishment.getType() == PunishmentType.BAN && punishment.isActive())) {
            Punishment punishment = profile.getPunishments().stream().filter(pun -> pun.getType() == PunishmentType.BAN && pun.isActive()).findFirst().get();
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, CC.translate("&cYou are currently banned for " + punishment.getReason() + ". Duration: " + (punishment.isPermanent() ? "Permanent" : DateUtils.formatTimeMillis(punishment.getDuration()))));
        } else if (profile.getPunishments().stream().anyMatch(punishment -> punishment.getType() == PunishmentType.BLACKLIST && punishment.isActive())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, CC.translate("&cYou are currently blacklisted."));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.setName(player.getName());
        profile.setOnline(true);

        profileService.loadPermissions(player);

        String joinMessage = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("on-join.messages.joined-the-game").replace("%player%", player.getName());
        String firstJoinMessage = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("on-join.messages.first-join").replace("%player%", player.getName());

        if (Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getBoolean("on-join.messages.welcome-message.enabled", true)) {
            List<String> welcomeMessages = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getStringList("on-join.messages.welcome-message.message");
            sendWelcomeMessage(player, welcomeMessages);
        }

        if (Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getBoolean("on-join.title-sender.enabled")) {
            String mainTitle = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("on-join.title-sender.main-title").replace("%player%", player.getName());
            String subTitle = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("on-join.title-sender.sub-title").replace("%player%", player.getName());

            player.sendTitle(CC.translate(mainTitle), CC.translate(subTitle));
        }

        if (profile.getStaffOptions().isVanish()) {
            event.setJoinMessage(null);
            return;
        }

        if (player.hasPlayedBefore()) {
            event.setJoinMessage(CC.translate(joinMessage));
        } else {
            event.setJoinMessage(CC.translate(firstJoinMessage));
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());
        profile.setOnline(false);
        profile.saveProfile();

        if (profile.getStaffOptions().isVanish()) {
            event.setQuitMessage(null);
            return;
        }

        String quitMessage = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("on-leave.messages.left-the-game").replace("%player%", player.getName());
        event.setQuitMessage(CC.translate(quitMessage));
    }

    /**
     * Sends the welcome message to the player
     *
     * @param player the player to send the message to
     */
    private void sendWelcomeMessage(Player player, List<String> messages) {
        for (String message : messages) {
            String replacedMessage = replaceWelcomeMessage(player, message);
            player.sendMessage(CC.translate(replacedMessage));
        }
    }

    /**
     * Replaces the placeholders in the welcome message
     *
     * @param player  the player to replace the placeholders for
     * @param message the message to replace the placeholders in
     * @return the message with the placeholders replaced
     */
    private String replaceWelcomeMessage(Player player, String message) {
        String bars = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml").getString("on-join.messages.bars-format");

        assert bars != null;
        return message
                .replace("%player%", player.getName())
                .replace("%health%", String.valueOf(Math.round(player.getHealth())))
                .replace("%bars%", bars);
    }
}
