package dev.revere.delta.feature.social;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.social.buttons.DiscordButton;
import dev.revere.delta.feature.social.buttons.TwitterButton;
import dev.revere.delta.feature.social.buttons.WebsiteButton;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import dev.revere.delta.util.menu.button.PageGlassButton;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class SocialMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("menus/socials-menu.yml")
                .getString("menu.title");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Delta plugin = Delta.getInstance();
        FileConfiguration config = plugin.getServiceManager().getService(ConfigService.class).getConfig("menus/socials-menu.yml");

        buttons.put(config.getInt("menu.items.twitter.slot"), new TwitterButton());
        buttons.put(config.getInt("menu.items.discord.slot"), new DiscordButton());
        buttons.put(config.getInt("menu.items.website.slot"), new WebsiteButton());

        addBorder(buttons, (byte) 6, 3);

        for (int slot = 0; slot < getSize(); slot++) {
            if (!buttons.containsKey(slot)) {
                buttons.put(slot, new PageGlassButton());
            }
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("menus/socials-menu.yml")
                .getInt("menu.rows");
    }
}
