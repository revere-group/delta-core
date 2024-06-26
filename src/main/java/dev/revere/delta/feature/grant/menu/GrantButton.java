package dev.revere.delta.feature.grant.menu;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.grant.GrantService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.DateUtils;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
@AllArgsConstructor
public class GrantButton extends Button {

    private final OfflinePlayer target;
    private final Grant grant;
    private boolean isActive;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (isActive) {
            return new ItemBuilder(Material.LIME_WOOL)
                    .name(grant.getRank().getNameColor() + StringUtils.capitalize(grant.getRank().getName()))
                    .lore("")
                    .lore(" &f● &bAdded by: &f" + grant.getAddedBy())
                    .lore(" &f● &bAdded at: &f" + DateUtils.formatDate(grant.getAddedAt()))
                    .lore(" &f● &bDuration: &f" + (grant.isPermanent() ? "Permanent" : grant.getExpiration()))
                    .lore("")
                    .lore(" &f● &bServer: &f" + grant.getServer())
                    .lore(" &f● &bReason: &f" + grant.getReason())
                    .lore(" &f● &bExpire: &f" + (grant.isPermanent() ? "Never" : grant.getExpirationDate()))
                    .lore("")
                    .lore(" &f● &bClick to remove this grant.")
                    .build();
        } else {
            return new ItemBuilder(Material.RED_WOOL)
                    .name(grant.getRank().getNameColor() + StringUtils.capitalize(grant.getRank().getName()))
                    .lore("")
                    .lore(" &f● &bAdded by: &f" + grant.getAddedBy())
                    .lore(" &f● &bAdded at: &f" + DateUtils.formatDate(grant.getAddedAt()))
                    .lore(" ")
                    .lore(" &f● &bRemoved by: &f" + (grant.getRemovedBy() == null ? "Console" : grant.getRemovedBy()))
                    .lore(" &f● &bRemoved at: &f" + DateUtils.formatDate(grant.getRemovedAt()))
                    .lore(" &f● &bReason: &f" + grant.getRemovedReason())
                    .lore("")
                    .lore(" &f● &bServer: &f" + grant.getServer())
                    .lore(" &f● &bExpired: &f" + (grant.hasExpired() ? "Expired" : grant.getExpirationDate()))
                    .lore("")
                    .build();
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (!isActive) return;
        GrantService grantService = Delta.getInstance().getServiceManager().getService(GrantService.class);
        grantService.removeGrant(grant, "No reason specified", target, player);

        new GrantMenu(true, player).openMenu(player);
    }
}
