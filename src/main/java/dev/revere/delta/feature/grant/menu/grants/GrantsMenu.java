package dev.revere.delta.feature.grant.menu.grants;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.grant.GrantService;
import dev.revere.delta.feature.grant.menu.grants.button.GrantsButton;
import dev.revere.delta.feature.grant.menu.grants.button.RefreshGrantsButton;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.menu.Button;
import dev.revere.delta.util.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
@AllArgsConstructor
public class GrantsMenu extends PaginatedMenu {

    private boolean showingActiveGrants;
    private final OfflinePlayer target;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("menus/grants-menu.yml").getString("title");
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        addGlassHeader(buttons);

        buttons.put(4, new RefreshGrantsButton(showingActiveGrants, target));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        GrantService grantService = Delta.getInstance().getServiceManager().getService(GrantService.class);
        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);

        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(target.getUniqueId());

        List<Grant> filteredGrants = showingActiveGrants ?
                grantService.getAllGrants(profile).stream().filter(Grant::isActive).toList() :
                grantService.getAllGrants(profile).stream().filter(grant -> !grant.isActive()).toList();


        int slot = 0;
        for (Grant grant : filteredGrants) {
            if (rankService.getRank(grant.getRankInformation().getName()) == null) {
                continue;
            }
            slot = validateSlot(slot);
            buttons.put(slot++, new GrantsButton(target, grant, showingActiveGrants));
        }

        addGlassToAvoidedSlots(buttons);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
