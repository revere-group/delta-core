package dev.revere.delta.feature.advancement.button;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.advancement.AdvancementCategory;
import dev.revere.delta.feature.advancement.AdvancementService;
import dev.revere.delta.feature.advancement.menu.CategoryMenu;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 30/06/2024 - 18:18
 */
@AllArgsConstructor
public class AdvancementButton extends Button {
    private final String name;
    private final ItemStack material;
    private final List<String> lore;
    private final Advancement advancement;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(material)
                .name(name)
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        AdvancementService advancementService = Delta.getInstance().getServiceManager().getService(AdvancementService.class);
        AdvancementCategory category = advancementService.getAdvancementCategory(advancement.getKey().getKey());

        if (!advancementService.hasAdvancement(player, advancement.getKey().getKey())) {
            player.sendMessage(CC.translate("&cYou have not completed this advancement yet."));
            playFail(player);
            return;
        }

        if (advancementService.hasAdvancementRedeemed(player, advancement.getKey().getKey())) {
            player.sendMessage(CC.translate("&cYou have already redeemed this advancement."));
            playFail(player);
            return;
        }

        advancementService.markAdvancementAsRedeemed(player, advancement.getKey().getKey());
        if (category != null) {
            int coinsReward = category.getReward();
            profile.setCoins(profile.getCoins() + coinsReward);
        } else {
            player.sendMessage(CC.translate("&cAn error occurred while redeeming the advancement."));
            playFail(player);
            return;
        }

        player.sendMessage(CC.translate("&fYou have redeemed the " + name + " &fadvancement and received &b" + category.getReward() + " coins&f."));
        playSuccess(player);
        new CategoryMenu(category).openMenu(player);
    }
}