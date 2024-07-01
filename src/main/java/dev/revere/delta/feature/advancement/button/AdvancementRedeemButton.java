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
public class AdvancementRedeemButton extends Button {
    private final String name;
    private final ItemStack material;
    private final List<String> lore;
    private final AdvancementCategory category;

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
        List<Advancement> advancements = advancementService.getAdvancements(category);

        int totalCoinsReward = 0;
        int redeemedCount = 0;

        for (Advancement advancement : advancements) {
            String advancementKey = advancement.getKey().getKey();
            if (advancementService.hasAdvancement(player, advancementKey) && !advancementService.hasAdvancementRedeemed(player, advancementKey)) {
                advancementService.markAdvancementAsRedeemed(player, advancementKey);
                totalCoinsReward += category.getReward();
                redeemedCount++;
            }
        }

        if (redeemedCount > 0) {
            profile.setCoins(profile.getCoins() + totalCoinsReward);
            profile.saveProfile();
            player.sendMessage(CC.translate("&fYou have redeemed &b" + redeemedCount + " &fadvancements in the &b" + category.getDisplayName() + " &fcategory and received &b" + totalCoinsReward + " coins&f."));
            playSuccess(player);
        } else {
            player.sendMessage(CC.translate("&cThere are no advancements to redeem in this category."));
            playFail(player);
        }

    }
}