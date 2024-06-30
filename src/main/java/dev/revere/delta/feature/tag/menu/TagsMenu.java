package dev.revere.delta.feature.tag.menu;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.advancement.menu.AdvancementsMenu;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
import dev.revere.delta.feature.tag.menu.button.ResetTagButton;
import dev.revere.delta.feature.tag.menu.button.TagButton;
import dev.revere.delta.profile.menu.ProfileMenu;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import dev.revere.delta.util.menu.button.BackButton;
import dev.revere.delta.util.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/29/2024
 */
public class TagsMenu extends PaginatedMenu {
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&8Tags Menu";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        addGlassHeader(buttons);

        buttons.put(4, new BackButton(new ProfileMenu()));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);

        buttons.put(3, new BackButton(new ProfileMenu()));
        buttons.put(5, new ResetTagButton());

        int slot = 0;

        for (Tag tag : tagService.getTags().stream().filter(tag -> tagService.hasTag(player, tag)).sorted(Comparator.comparingInt(Tag::getWeight).reversed()).toList()) {
            slot = validateSlot(slot);
            buttons.put(slot++, new TagButton(tag));
        }

        addGlassToAvoidedSlots(buttons);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
