package dev.revere.delta.feature.server.whitelist;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.feature.server.ServerService;
import dev.revere.delta.feature.server.whitelist.listener.WhitelistListener;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
@Getter
public class WhitelistService implements IService {

    private final Delta plugin;
    private final ServerService serverService;

    public WhitelistService(Delta plugin) {
        this.plugin = plugin;
        this.serverService = Delta.getInstance().getServiceManager().getService(ServerService.class);
    }

    @Override
    public void register() {
        plugin.getServer().getPluginManager().registerEvents(new WhitelistListener(), plugin);
    }

    /**
     * Enable or disable the whitelist for the current server.
     *
     * @param enabled true to enable whitelist, false to disable
     */
    public void setWhitelistEnabled(boolean enabled) {
        String serverName = serverService.getServerName();
        serverService.setWhitelistEnabled(serverName, enabled);
    }

    /**
     * Check if the whitelist is enabled on the current server.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isWhitelistEnabled() {
        String serverName = serverService.getServerName();
        return serverService.isWhitelistEnabled(serverName);
    }

    /**
     * Check if a player is whitelisted on the current server.
     *
     * @param player the player to check
     * @return true if whitelisted, false otherwise
     */
    public boolean isWhitelisted(Player player) {
        String serverName = plugin.getServiceManager().getService(ServerService.class).getServerName();
        ServerService serverService = plugin.getServiceManager().getService(ServerService.class);
        return !serverService.isWhitelistEnabled(serverName) || player.hasPermission("delta.whitelist.bypass");
    }
}
