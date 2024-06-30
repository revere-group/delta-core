package dev.revere.delta.feature.rank;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.server.ServerService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
@Getter
@Setter
@AllArgsConstructor
public class Rank {
    private final String name;
    private String prefix;
    private String suffix;

    private int weight;
    private int cost;

    private boolean defaultRank;
    private boolean staffRank;
    private boolean purchasable;
    private boolean global;
    private boolean hidden;

    private List<String> inheritance;
    private List<String> permissions;
    private List<String> servers;
    private ChatColor nameColor;

    /**
     * Constructor for the Rank class
     *
     * @param name the name of the rank
     */
    public Rank(String name) {
        this.name = name;
    }

    /**
     * Check if the rank has the server
     *
     * @param name the name of the server
     * @return true if the rank has the server, false otherwise
     */
    public boolean hasServer(String name) {
        return this.servers.stream().filter(server -> server.equalsIgnoreCase(name)).findFirst().orElse(null) != null;
    }

    /**
     * Check if the rank is accessible on the server
     *
     * @return true if the rank is accessible on the server, false otherwise
     */
    public boolean isAccessibleOnServer() {
        return this.global || hasServer(Delta.getInstance().getServiceManager().getService(ServerService.class).getServerName());
    }

    /**
     * Check if the rank is accessible on the server
     *
     * @param serverName the name of the server
     * @return true if the rank is accessible on the server, false otherwise
     */
    public boolean isAccessibleOnServer(String serverName) {
        return this.global || hasServer(serverName);
    }
}
