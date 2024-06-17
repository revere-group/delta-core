package dev.revere.revsentials.service;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.service.Service;
import dev.revere.revsentials.profile.Profile;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
@Getter
public class ProfileService implements Service {

    private final Map<UUID, Profile> profiles = new HashMap<>();

    private final Revsential plugin;

    /**
     * Constructor for the ProfileService class
     *
     * @param plugin the main class of the plugin
     */
    public ProfileService(Revsential plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
    }

    /**
     * Get a profile by its UUID
     *
     * @param uuid the UUID of the profile
     * @return the profile
     */
    public Profile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    /**
     * Add or update a profile by its UUID.
     *
     * @param uuid    the UUID of the profile
     * @param profile the profile to add or update
     */
    public void addProfile(UUID uuid, Profile profile) {
        profiles.put(uuid, profile);
    }

    /**
     * Remove a profile by its UUID.
     *
     * @param uuid the UUID of the profile to remove
     */
    public void removeProfile(UUID uuid) {
        profiles.remove(uuid);
    }
}
