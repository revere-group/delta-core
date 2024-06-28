package dev.revere.delta.feature.grant.menu.grants.button;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.grant.GrantService;
import dev.revere.delta.feature.grant.menu.grants.GrantsMenu;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.DateUtils;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
@AllArgsConstructor
public class GrantsButton extends Button {

    private final OfflinePlayer target;
    private final Grant grant;
    private boolean isActive;

    @Override
    public ItemStack getButtonItem(Player player) {
        FileConfiguration config = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("menus/grants-menu.yml");
        if (isActive) {
            return new ItemBuilder(Material.matchMaterial(config.getString("grant-button.active.material")))
                    .name(config.getString("grant-button.active.name").replace("%rank%", grant.getRankInformation().getNameColor() + StringUtils.capitalize(grant.getRankInformation().getName())))
                    .lore(config.getStringList("grant-button.active.lore")
                            .stream()
                            .map(line -> line
                                    .replace("%added_by%", grant.getAddedBy())
                                    .replace("%added_at%", DateUtils.formatDate(grant.getAddedAt()))
                                    .replace("%duration%", grant.isPermanent() ? "Permanent" : grant.getExpiration())
                                    .replace("%server%", grant.getServer())
                                    .replace("%reason%", grant.getReason())
                                    .replace("%rank%", grant.getRankInformation().getNameColor() + StringUtils.capitalize(grant.getRankInformation().getName()))
                                    .replace("%duration%", grant.isPermanent() ? "Permanent" : grant.getExpiration())
                                    .replace("%expired%", grant.hasExpired() ? "true" : "false"))
                            .toArray(String[]::new))
                    .durability(config.getInt("grant-button.active.data"))
                    .build();
        } else {
            return new ItemBuilder(Material.matchMaterial(config.getString("grant-button.inactive.material")))
                    .name(config.getString("grant-button.inactive.name").replace("%rank%", StringUtils.capitalize(grant.getRankInformation().getName())))
                    .lore(config.getStringList("grant-button.inactive.lore")
                            .stream()
                            .map(line -> line
                                    .replace("%added_by%", grant.getAddedBy())
                                    .replace("%added_at%", DateUtils.formatDate(grant.getAddedAt()))
                                    .replace("%removed_by%", grant.getRemovedBy() == null ? "Console" : grant.getRemovedBy())
                                    .replace("%removed_at%", DateUtils.formatDate(grant.getRemovedAt()))
                                    .replace("%removed_reason%", grant.getRemovedReason())
                                    .replace("%server%", grant.getServer()))
                            .toArray(String[]::new))
                    .durability(config.getInt("grant-button.inactive.data"))
                    .build();
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;
        if (!isActive) return;
        GrantService grantService = Delta.getInstance().getServiceManager().getService(GrantService.class);
        grantService.removeGrant(grant, "No reason specified", target, player);

        new GrantsMenu(true, player).openMenu(player);
    }
}
