package dev.revere.delta.feature.shop.menus;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.feature.shop.menus.buttons.CoinShopButton;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import dev.revere.delta.util.menu.button.BackButton;
import dev.revere.delta.util.menu.button.PageGlassButton;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 19:10
 */
@AllArgsConstructor
public class CoinShopMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Coin Shop > Main";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        buttons.put(11, new CoinShopButton("&b&lRanks", Material.DIAMOND, 0, Arrays.asList(
                "",
                " &f● &bAvailable Ranks: &f" + rankService.getRanks().size(),
                " &f● &bYour Rank: &f" + rankService.getHighestRank(profile).getNameColor() + rankService.getHighestRank(profile).getName(),
                "",
                " &f● &bBalance: &f$" + profile.getCoins(),
                "",
                " &f► Click to view all &bavailable &franks!",
                "")));

        buttons.put(13, new CoinShopButton("&b&lTags", Material.NAME_TAG, 0, Arrays.asList(
                "",
                " &f● &bAvailable Tags: &fnull",
                " &f● &bYour Tag: &fnull",
                "",
                " &f● &bBalance: &f$" + profile.getCoins(),
                "",
                " &f► Click to view all &bavailable &ftags!",
                "")));

        buttons.put(15, new CoinShopButton("&c&lEmpty", Material.CHEST, 0, Arrays.asList(
                "",
                "&7Empty",
                "")));

        addGlass(buttons);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}
