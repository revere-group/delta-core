package dev.revere.delta.feature.grant;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project Delta
 * @date 6/25/2024
 */
@Getter
@Setter
public class GrantService implements IService {

    private final Delta plugin;

    /**
     * Constructor for the GrantService class
     *
     * @param plugin the main class of the plugin
     */
    public GrantService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {

    }

    /**
     * Add a grant to a profile.
     *
     * @param grant the grant to add
     * @param uuid  the UUID of the profile
     */
    public void addGrant(Grant grant, UUID uuid) {
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(uuid);

        List<Grant> grants = profile.getGrants();
        grants = grants == null ? new ArrayList<>() : new ArrayList<>(grants);
        grants.add(grant);

        profile.setGrants(grants);

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            profileService.loadPermissions(player);
        }

        profile.saveProfile();
    }

    /**
     * Remove a grant from a profile.
     *
     * @param rankName the name of the rank to remove
     * @param uuid     the UUID of the profile
    * @param sender   the sender of the command
     */
    public void removeGrant(String rankName, String reason, UUID uuid, CommandSender sender) {
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(uuid);

        List<Grant> grants = profile.getGrants();
        grants.stream().filter(grant -> grant.getRankName().equalsIgnoreCase(rankName)).forEach(grant -> {
            grant.setRemovedBy(sender.getName());
            grant.setRemovedAt(System.currentTimeMillis());
            grant.setRemovedReason(reason);
            grant.setActive(false);
        });

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            profileService.loadPermissions(player);
        }

        profile.saveProfile();
    }

    /**
     * Remove a grant from a profile.
     *
     * @param grant  the grant to remove
     * @param reason the reason for removing the grant
     * @param target the target of the command
     * @param sender the sender of the command
     */
    public void removeGrant(Grant grant, String reason, OfflinePlayer target, Player sender) {
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(target.getUniqueId());

        grant.setRemovedBy(sender.getName());
        grant.setRemovedAt(System.currentTimeMillis());
        grant.setRemovedReason(reason);
        grant.setActive(false);

        profile.saveProfile();
    }

    /**
     * Get a specific active grant by rank.
     *
     * @param rank the rank of the grant to retrieve
     * @return an optional containing the grant if found, otherwise empty
     */
    public Grant getActiveGrant(Profile profile, String rank) {
        return profile.getGrants().stream()
                .filter(Grant::isActive)
                .filter(grant -> grant.getRankName().equalsIgnoreCase(rank)).findFirst().orElse(null);
    }

    /**
     * Get a specific active grant by rank.
     *
     * @param rank the rank of the grant to retrieve
     * @return an optional containing the grant if found, otherwise empty
     */
    public Grant getActiveGrant(UUID uuid, String rank) {
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(uuid);
        return getActiveGrant(profile, rank);
    }

    /**
     * Get a specific inactive grant by rank.
     *
     * @param rank the rank of the grant to retrieve
     * @return an optional containing the grant if found, otherwise empty
     */
    public Grant getInactiveGrant(Profile profile, String rank) {
        return profile.getGrants().stream()
                .filter(grant -> !grant.isActive())
                .filter(grant -> grant.getRankName().equalsIgnoreCase(rank)).findFirst().orElse(null);
    }

    /**
     * Get the highest rank of a player profile.
     *
     * @param profile the profile to get the highest rank for
     * @return the highest rank
     */
    public Grant getHighestGrant(Profile profile) {
        return this.getActiveGrants(profile).stream().filter(grant -> grant.getRankName() != null).max(Comparator.comparingInt(grant -> grant.getRank().getWeight())).orElse(null);
    }

    /**
     * Get all active grants for a profile.
     *
     * @param profile the profile to get the grants for
     * @return a list of active grants
     */
    public List<Grant> getActiveGrants(Profile profile) {
        return profile.getGrants().stream().filter(grant -> !grant.hasExpired() && grant.isActive()).toList();
    }

    /**
     * Get all grants for a profile.
     *
     * @param profile the profile to get the grants for
     * @return a list of grants
     */
    public List<Grant> getAllGrants(Profile profile) {
        return profile.getGrants();
    }
}
