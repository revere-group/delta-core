package dev.revere.delta.feature.shop.buttons;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.shop.menus.CoinShopRankMenu;
import dev.revere.delta.feature.shop.menus.CoinShopTagMenu;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Delta
 * @date 26/06/2024 - 23:18
 */
public class CoinShopButton extends Button {
    private final String name;
    private final Material material;
    private final int data;
    private final List<String> lore;

    public CoinShopButton(String name, Material material, int data, List<String> lore) {
        this.name = name;
        this.material = material;
        this.data = data;
        this.lore = lore;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(material)
                .name(name)
                .durability(data)
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        switch (material) {
            case NETHER_STAR:
                new CoinShopRankMenu().openMenu(player);
                break;
            case NAME_TAG:
                new CoinShopTagMenu().openMenu(player);
                break;
            case EMERALD:
                long currentTime = System.currentTimeMillis();
                if (!profile.isDailyRewardClaimable()) {
                    playFail(player);
                    player.sendMessage(CC.translate("&cYou have already claimed your daily reward today!"));
                    return;
                }

                profile.setLastDailyReward(currentTime);
                profile.setCoins(profile.getCoins() + 50);
                profile.saveProfile();

                player.sendMessage(CC.translate("&aYou have claimed your daily reward of $50!"));
                playSuccess(player);
                break;
        }
    }
}