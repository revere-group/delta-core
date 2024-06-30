package dev.revere.delta.feature.shop.buttons;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.shop.menus.CoinShopTagMenu;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 19:43
 */
@AllArgsConstructor
public class CoinShopTagButton extends Button {

    private Tag tag;

    @Override
    public ItemStack getButtonItem(Player player) {
        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);

        String purchaseLore = tagService.hasTag(player, tag) ? "&cYou already own this tag" : "&f► Click to " + tag.getColor() + "purchase &fthis tag!";

        return new ItemBuilder(Material.LIME_WOOL)
                .name(tag.getColor() + tag.getName())
                .lore(Arrays.asList(
                        "&8&m----------------------",
                        CC.translate(" &fPrefix: " + tag.getPrefix()),
                        " &fPrice: " + tag.getColor() + "$" + tag.getCost(),
                        "",
                        purchaseLore,
                        "&8&m----------------------"))
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        TagService tagService = Delta.getInstance().getServiceManager().getService(TagService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (tagService.hasTag(player, tag)) {
            player.sendMessage(CC.translate("&cYou already own this tag!"));
            playFail(player);
            return;
        }

        if (profile.getCoins() < tag.getCost()) {
            player.sendMessage(CC.translate("&cYou do not have enough coins to purchase this tag!"));
            playFail(player);
            return;
        }

        profile.setCoins(profile.getCoins() - tag.getCost());

        List<String> permissions = profile.getPermissions();
        permissions.add(tagService.getPermission(tag));

        profile.setPermissions(permissions);
        profile.saveProfile();

        profileService.loadPermissions(player);

        player.sendMessage(CC.translate("&aYou have successfully purchased the tag " + tag.getName() + "!"));
        playSuccess(player);
        new CoinShopTagMenu().openMenu(player);
    }
}
