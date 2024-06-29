package dev.revere.delta.feature.shop.menus;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.feature.shop.menus.buttons.CoinShopRankButton;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import dev.revere.delta.util.menu.button.PageGlassButton;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 19:41
 */
@AllArgsConstructor
public class CoinShopRankMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Coin Shop > Ranks";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        int starterSlot = 10;

        List<Rank> sortedRanks = rankService.getRanks().stream()
                .filter(Rank::isPurchasable)
                .sorted((r1, r2) -> Integer.compare(r2.getWeight(), r1.getWeight()))
                .toList();

        for (Rank rank : sortedRanks) {
            buttons.put(starterSlot++, new CoinShopRankButton(rank));
        }

        addGlass(buttons);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
