package dev.revere.delta.feature.tag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.ChatColor;

/**
 * @author Remi
 * @project Delta
 * @date 6/29/2024
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Tag {

    private final String name;
    private String prefix;

    private ChatColor color = ChatColor.WHITE;

    private boolean purchasable;

    private int weight;
    private int cost;
}
