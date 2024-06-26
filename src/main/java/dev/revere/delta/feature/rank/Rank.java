package dev.revere.delta.feature.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

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
    private boolean defaultRank;
    private boolean staffRank;

    private List<String> inheritance;
    private List<String> permissions;
    private ChatColor nameColor;

    /**
     * Constructor for the Rank class
     *
     * @param name the name of the rank
     */
    public Rank(String name) {
        this.name = name;
    }
}
