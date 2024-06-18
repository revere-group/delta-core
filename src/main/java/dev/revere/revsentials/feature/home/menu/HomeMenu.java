package dev.revere.revsentials.feature.home.menu;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.util.menu.Button;
import dev.revere.revsentials.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/18/2024
 */
public class HomeMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&8Home Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        Revsential.getInstance().getHomeRepository().getHomes(player).forEach(home -> {
            buttons.put(buttons.size() + 10, new HomeButton(home));
        });

        addBorder(buttons, (byte) 6, 3);

        return buttons;
    }
}
