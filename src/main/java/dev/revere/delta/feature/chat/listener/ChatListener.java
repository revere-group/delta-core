package dev.revere.delta.feature.chat.listener;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.grant.GrantService;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Remi
 * @project Delta
 * @date 6/19/2024
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        Profile profile  = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        FileConfiguration config = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("messages.yml");

        boolean translate = player.hasPermission("delta.chat.color");
        boolean isStaff = player.hasPermission("delta.staff");

        String chatFormat = isStaff ? CC.translate(config.getString("chat.format.staff")) : CC.translate(config.getString("chat.format.regular"));

        chatFormat = chatFormat.replace("%color%", profile.getNameColor());
        chatFormat = chatFormat.replace("%player%", player.getDisplayName());
        chatFormat = translate ? chatFormat.replace("%message%", CC.translate(event.getMessage())) : chatFormat.replace("%message%", event.getMessage());
        chatFormat = chatFormat.replace("%clan%", CC.translate(Delta.getInstance().getClanRepository().getPlayerClan(player.getUniqueId()) != null ? "&f[" + Delta.getInstance().getClanRepository().getPlayerClan(player.getUniqueId()).getColoredName() + "&f] ": ""));
        chatFormat = chatFormat.replace("%rank%", CC.translate(Delta.getInstance().getServiceManager().getService(RankService.class).getHighestRank(profile).getPrefix()));
        event.setFormat(chatFormat);
    }
}
