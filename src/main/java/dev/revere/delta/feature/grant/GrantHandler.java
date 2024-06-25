package dev.revere.delta.feature.grant;

import dev.revere.delta.Delta;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 21:55
 */

public class GrantHandler {

    public static void addGrant(Grant grant, UUID uuid) {
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(uuid);

        List<Grant> grants = profile.getGrants();

        if (grants == null) {
            grants = new ArrayList<>();
        } else {
            grants = new ArrayList<>(grants);
        }

        grants.add(grant);
        profile.setGrants(grants);
    }

    public static void removeGrant(Grant grant, UUID uuid) {
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(uuid);

        List<Grant> grants = profile.getGrants();
        if (grants != null) {
            grants.removeIf(g -> g.getRank().equalsIgnoreCase(grant.getRank()));
            profile.setGrants(grants);
        }
    }

}