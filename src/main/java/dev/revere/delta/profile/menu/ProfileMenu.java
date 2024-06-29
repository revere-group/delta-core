package dev.revere.delta.profile.menu;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/29/2024
 */
@AllArgsConstructor
public class ProfileMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&8Profile Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

        if (skullMeta != null) {
            skullMeta.setOwningPlayer(player);
            skullItem.setItemMeta(skullMeta);
        }

        buttons.put(4, new ProfileButton("&b&lYour Profile", skullItem, Arrays.asList(
                "&8&m----------------------",
                " &fYour Name: &b" + player.getName(),
                " &fBalance: &b$" + profile.getCoins(),
                "",
                " &fRank: " + rankService.getHighestRank(profile).getNameColor() + rankService.getHighestRank(profile).getName(),
                " &fTag: " + (profile.getTag() != null ? profile.getTag().getColor() + profile.getTag().getName() : "&bNone"),
                "",
                "&aClick to view!",
                "&8&m----------------------"
        )));

        buttons.put(19, new ProfileButton("&b&lProfile Settings", new ItemStack(Material.ANVIL), Arrays.asList(
                "&8&m----------------------",
                " &fCustomize your settings",
                " &fto your preference.",
                "",
                "&aClick to view!",
                "&8&m----------------------"
        )));

        buttons.put(21, new ProfileButton("&b&lTag Customization", new ItemStack(Material.NAME_TAG), Arrays.asList(
                "&8&m----------------------",
                " &fCustomize your tag",
                " &fto your preference.",
                "",
                "&aClick to view!",
                "&8&m----------------------"
        )));

        buttons.put(23, new ProfileButton("&b&lQuests", new ItemStack(Material.BEACON), Arrays.asList(
                "&8&m----------------------",
                " &fView your current quests",
                " &fand complete them for rewards.",
                "",
                "&aClick to view!",
                "&8&m----------------------"
        )));

        buttons.put(25, new ProfileButton("&b&lCoin Shop", new ItemStack(Material.EMERALD), Arrays.asList(
                "&8&m----------------------",
                " &fPurchase cosmetics,",
                " &fthemes, and more with",
                " &fthe coins you earn.",
                "",
                "&aClick to view!",
                "&8&m----------------------"
        )));

        addBorder(buttons, (byte) 6, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
