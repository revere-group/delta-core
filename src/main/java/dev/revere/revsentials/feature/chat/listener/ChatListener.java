package dev.revere.revsentials.feature.chat.listener;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.service.ConfigService;
import dev.revere.revsentials.util.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/19/2024
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        FileConfiguration config = Revsential.getInstance().getServiceManager().getService(ConfigService.class).getConfigByName("messages.yml");

        boolean translate = player.hasPermission("revsentials.chat.color");
        boolean isStaff = player.hasPermission("revsentials.staff");

        String chatFormat = isStaff ? CC.translate(config.getString("chat.format.staff")) : CC.translate(config.getString("chat.format.regular"));

        chatFormat = chatFormat.replace("%player%", player.getDisplayName());
        chatFormat = translate ? chatFormat.replace("%message%", CC.translate(event.getMessage())) : chatFormat.replace("%message%", event.getMessage());
        chatFormat = chatFormat.replace("%clan%", CC.translate(Revsential.getInstance().getClanRepository().getPlayerClan(player.getUniqueId()) != null ? "&f[" + Revsential.getInstance().getClanRepository().getPlayerClan(player.getUniqueId()).getColoredName() + "&f] ": ""));

        event.setFormat(chatFormat);
    }
}
