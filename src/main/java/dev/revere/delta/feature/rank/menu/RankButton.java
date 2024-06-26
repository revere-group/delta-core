package dev.revere.delta.feature.rank.menu;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.CC;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.ItemBuilder;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
@AllArgsConstructor
public class RankButton extends Button {

    private final Rank rank;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.WHITE_WOOL)
                .name("&b&lRank Information:")
                .lore(" &f● &bName: &7" + StringUtils.capitalize(rank.getName()))
                .lore(" &f● &bPrefix: &7" + rank.getPrefix())
                .lore(" &f● &bSuffix: &7" + rank.getSuffix())
                .lore(" &f● &bWeight: &7" + rank.getWeight())
                .lore(" &f● &bColor: &7" + rank.getNameColor() + rank.getNameColor().name())
                .lore(" &f● &bStaff: &7" + rank.isStaffRank())
                .lore(" &f● &bDefault: &7" + rank.isDefaultRank())
                .lore(" &f● &bPermissions: &7" + rank.getPermissions().size())
                .lore("")
                .lore("&b&lActions:")
                .lore(" &f● &bLeft Click &7| &fView permissions")
                .lore(" &f● &bRight Click &7| &fDelete rank")
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        switch (clickType) {
            case LEFT:
                if (rank.getPermissions().isEmpty()) {
                    player.sendMessage(CC.translate("&cThis rank has no permissions."));
                    return;
                }

                player.sendMessage("");
                player.sendMessage(CC.translate("&b&l" + rank.getName() + " &7Permissions:"));
                rank.getPermissions().forEach(permission -> player.sendMessage(CC.translate(" &f● &7" + permission)));
                break;
            case RIGHT:
                Delta.getInstance().getServiceManager().getService(RankService.class).deleteRank(rank);
                player.sendMessage(CC.translate("&fSuccessfully deleted the rank &b" + rank.getName() + "&f."));
                new RankMenu().openMenu(player);
                break;
        }
    }
}
