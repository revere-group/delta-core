package dev.revere.delta.feature.shop.buttons;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.grant.GrantService;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.feature.server.ServerService;
import dev.revere.delta.feature.shop.menus.CoinShopRankMenu;
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

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 19:43
 */
@AllArgsConstructor
public class CoinShopRankButton extends Button {

    private Rank rank;

    @Override
    public ItemStack getButtonItem(Player player) {
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        boolean hasRank = Delta.getInstance().getServiceManager().getService(RankService.class).getHighestRank(profile).getWeight() >= rank.getWeight();

        String purchaseLore = hasRank ? "&cYou already own this rank or a higher rank!" : "&f► Click to " + rank.getNameColor() + "purchase &fthis rank!";

        return new ItemBuilder(Material.LIME_WOOL)
                .name(rank.getNameColor() + rank.getName())
                .lore(Arrays.asList(
                        "&8&m----------------------",
                        CC.translate(" &fPrefix: &b" + rank.getPrefix()),
                        " &fPrice: " + rank.getNameColor() + "$" + rank.getCost(),
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
        Profile profile = profileService.getProfile(player.getUniqueId());

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        if (rankService.getHighestRank(profile).getWeight() >= rank.getWeight()) {
            player.sendMessage(CC.translate("&cYou already own this rank or a higher rank!"));
            playFail(player);
            return;
        }

        if (profile.getCoins() < rank.getCost()) {
            player.sendMessage(CC.translate("&cYou do not have enough coins to purchase this rank!"));
            playFail(player);
            return;
        }

        profile.setCoins(profile.getCoins() - rank.getCost());

        GrantService grantService = Delta.getInstance().getServiceManager().getService(GrantService.class);

        Grant grant = new Grant();
        grant.setRankName(rank.getName());
        grant.setServer(Delta.getInstance().getServiceManager().getService(ServerService.class).getServerName());
        grant.setReason("Purchased");
        grant.setAddedBy("Console");
        grant.setRemovedBy(null);
        grant.setRemovedReason(null);
        grant.setAddedAt(System.currentTimeMillis());
        grant.setPermanent(true);
        grant.setActive(true);
        grantService.addGrant(grant, player.getUniqueId());

        player.sendMessage(CC.translate("&aYou have successfully purchased the rank " + rank.getName() + "!"));
        playSuccess(player);
        new CoinShopRankMenu().openMenu(player);
    }
}
