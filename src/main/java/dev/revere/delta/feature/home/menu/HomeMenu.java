package dev.revere.delta.feature.home.menu;

import dev.revere.delta.Delta;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/18/2024
 */
public class HomeMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("menus/homes-menu.yml").getString("title");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        Delta.getInstance().getHomeRepository().getHomes(player).forEach(home -> {
            buttons.put(buttons.size() + 10, new HomeButton(home));
        });

        addBorder(buttons, (byte) 6, 3);

        return buttons;
    }
}
