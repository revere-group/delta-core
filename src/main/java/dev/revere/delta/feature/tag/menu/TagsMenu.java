package dev.revere.delta.feature.tag.menu;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
import dev.revere.delta.feature.tag.menu.button.ResetTagButton;
import dev.revere.delta.feature.tag.menu.button.TagButton;
import dev.revere.delta.profile.menu.ProfileMenu;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import dev.revere.delta.util.menu.button.BackButton;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/29/2024
 */
public class TagsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&8Tags Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);

        buttons.put(3, new BackButton(new ProfileMenu()));
        buttons.put(5, new ResetTagButton());

        int slot = 10;

        for (Tag tag : tagService.getTags().stream().filter(tag -> tagService.hasTag(player, tag)).sorted(Comparator.comparingInt(Tag::getWeight).reversed()).toList()) {
            buttons.put(slot++, new TagButton(tag));
        }

        addGlass(buttons);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
