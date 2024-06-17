package dev.revere.revsentials.menu.social;

import dev.revere.revsentials.menu.social.buttons.DiscordButton;
import dev.revere.revsentials.menu.social.buttons.TwitterButton;
import dev.revere.revsentials.menu.social.buttons.WebsiteButton;
import dev.revere.revsentials.util.menu.Button;
import dev.revere.revsentials.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class SocialMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&8Social Media Links";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new TwitterButton());
        buttons.put(13, new DiscordButton());
        buttons.put(15, new WebsiteButton());

        addBorder(buttons, (byte) 6, 3);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}
