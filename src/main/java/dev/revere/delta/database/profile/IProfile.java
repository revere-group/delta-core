package dev.revere.delta.database.profile;

import dev.revere.delta.profile.Profile;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public interface IProfile {

    /**
     * Load a profile
     *
     * @param profile the profile to load
     */
    void loadProfile(Profile profile);

    /**
     * Save a profile
     *
     * @param profile the profile to save
     */
    void saveProfile(Profile profile);
}
