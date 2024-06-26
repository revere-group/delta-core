package dev.revere.delta.feature.grant.menu.grant;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.grant.menu.grant.button.ViewGrantsButton;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.button.CancelButton;
import dev.revere.delta.util.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Delta
 * @date 26/06/2024 - 22:58
 */
@AllArgsConstructor
public class GrantMenu extends PaginatedMenu {

    private OfflinePlayer targetPlayer;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Grant " + targetPlayer.getName();
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(targetPlayer.getUniqueId());

        addGlassHeader(buttons);

        buttons.put(4, new ViewGrantsButton(targetPlayer,"&fView &b" + targetPlayer.getName() + "&f's grants", Material.NAME_TAG, 0, Arrays.asList(
                "",
                " &b" + targetPlayer.getName() + "&f's current Rank is &b" +
                        rankService.getHighestRank(profile).getNameColor() +
                        rankService.getHighestRank(profile).getName() + "&f.",
                ""
        )));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        return buttons;
    }


}
