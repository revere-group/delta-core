package dev.revere.delta.feature.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
@Getter
@Setter
@AllArgsConstructor
public class Home {
    private String name;
    private Location location;
}
