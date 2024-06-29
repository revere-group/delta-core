package dev.revere.delta.feature.shop.menus;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.shop.buttons.CoinShopTagButton;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import dev.revere.delta.util.menu.button.BackButton;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 19:41
 */
@AllArgsConstructor
public class CoinShopTagMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Coin Shop > Tags";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);

        int starterSlot = 10;

        buttons.put(4, new BackButton(new CoinShopMenu()));

        List<Tag> sortedTags = tagService.getTags().stream()
                .filter(Tag::isPurchasable)
                .sorted(Comparator.comparingInt(Tag::getWeight).reversed())
                .toList();

        for (Tag tag : sortedTags) {
            buttons.put(starterSlot++, new CoinShopTagButton(tag));
        }

        addGlass(buttons);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
