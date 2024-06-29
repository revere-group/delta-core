package dev.revere.delta.feature.shop.menus;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.feature.shop.buttons.CoinShopButton;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.profile.menu.ProfileMenu;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import dev.revere.delta.util.menu.button.BackButton;
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
        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        buttons.put(4, new BackButton(new ProfileMenu()));

        buttons.put(20, new CoinShopButton("&b&lRanks", Material.NETHER_STAR, 0, Arrays.asList(
                "&8&m----------------------",
                " &fAvailable Ranks: &b" + rankService.getRanks().stream().filter(Rank::isPurchasable).toList().size(),
                " &fYour Rank: " + rankService.getHighestRank(profile).getNameColor() + rankService.getHighestRank(profile).getName(),
                "",
                " &fBalance: &b$" + profile.getCoins(),
                "",
                "&aClick to view!",
                "&8&m----------------------")));

        buttons.put(22, new CoinShopButton("&b&lTags", Material.NAME_TAG, 0, Arrays.asList(
                "&8&m----------------------",
                " &fAvailable Tags: &b" + tagService.getTags().stream().filter(Tag::isPurchasable).toList().size(),
                " &fYour Tag: " + (profile.getTag() != null ? (profile.getTag().getColor() + profile.getTag().getName()) : "&bNone"),
                "",
                " &fBalance: &b$" + profile.getCoins(),
                "",
                "&aClick to view!",
                "&8&m----------------------")));

        buttons.put(24, new CoinShopButton("&b&lDaily Reward", Material.EMERALD, 0, Arrays.asList(
                "&8&m----------------------",
                " &fClaim your daily reward!",
                "",
                " &fTime: &bEvery 24 hours",
                " &fReward: &b$50",
                "",
                (profile.isDailyRewardClaimable() ? "&aClick to claim your reward!" : "&cYou can claim in " + profile.getDailyRewardRemainingTime()),
                "&8&m----------------------")));

        addBorder(buttons, (byte) 6, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
