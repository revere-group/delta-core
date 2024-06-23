package dev.revere.delta.feature.rank.menu;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import dev.revere.delta.util.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class RankMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&8Rank Menu";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        Delta.getInstance().getServiceManager().getService(RankService.class).getRanks().forEach(rank -> {
            buttons.put(buttons.size(), new RankButton(rank));
        });

        return buttons;
    }
}
