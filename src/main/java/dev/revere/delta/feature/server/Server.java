package dev.revere.delta.feature.server;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

/**
 * @author Remi
 * @project Delta
 * @date 6/28/2024
 */
@Getter
@Setter
public class Server {

    private String name;
    private Location spawnLocation;
    private boolean whitelistEnabled;

    public Server(String name) {
        this.name = name;
    }
}
