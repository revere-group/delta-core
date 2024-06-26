package dev.revere.delta.feature.server.whitelist.listener;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.server.whitelist.WhitelistService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
public class WhitelistListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        WhitelistService whitelistService = Delta.getInstance().getServiceManager().getService(WhitelistService.class);

        if (!whitelistService.isWhitelisted(player)) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "You are not whitelisted to join this server.");
        }
    }
}
