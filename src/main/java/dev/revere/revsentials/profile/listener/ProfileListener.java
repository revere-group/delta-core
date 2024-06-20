package dev.revere.revsentials.profile.listener;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.profile.Profile;
import dev.revere.revsentials.service.ProfileService;
import dev.revere.revsentials.util.CC;
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
 * @project Revsential
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
        Revsential.getInstance().getServiceManager().getService(ProfileService.class).addProfile(profile.getUuid(), profile);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Profile profile = Revsential.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());
        profile.setName(player.getName());
        profile.setOnline(true);

        String joinMessage = Revsential.getInstance().getConfig("messages.yml").getString("on-join.messages.joined-the-game").replace("%player%", player.getName());
        String firstJoinMessage = Revsential.getInstance().getConfig("messages.yml").getString("on-join.messages.first-join").replace("%player%", player.getName());

        if (player.hasPlayedBefore()) {
            event.setJoinMessage(CC.translate(joinMessage));
        } else {
            event.setJoinMessage(CC.translate(firstJoinMessage));
        }

        if (Revsential.getInstance().getConfig("messages.yml").getBoolean("on-join.messages.welcome-message.enabled", true)) {
            List<String> welcomeMessages = Revsential.getInstance().getConfig("messages.yml").getStringList("on-join.messages.welcome-message.message");
            sendWelcomeMessage(player, welcomeMessages);
        }

        if (Revsential.getInstance().getConfig("messages.yml").getBoolean("on-join.title-sender.enabled")) {
            String mainTitle = Revsential.getInstance().getConfig("messages.yml").getString("on-join.title-sender.main-title").replace("%player%", player.getName());
            String subTitle = Revsential.getInstance().getConfig("messages.yml").getString("on-join.title-sender.sub-title").replace("%player%", player.getName());

            player.sendTitle(CC.translate(mainTitle), CC.translate(subTitle));
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Revsential.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());
        profile.setOnline(false);

        String quitMessage = Revsential.getInstance().getConfig("messages.yml").getString("on-leave.messages.left-the-game").replace("%player%", player.getName());
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
        String bars = Revsential.getInstance().getConfig("messages.yml").getString("on-join.messages.bars-format");

        assert bars != null;
        return message
                .replace("%player%", player.getName())
                .replace("%health%", String.valueOf(Math.round(player.getHealth())))
                .replace("%bars%", bars);
    }
}
