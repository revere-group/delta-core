package dev.revere.delta.database.profile.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.revere.delta.Delta;
import dev.revere.delta.database.profile.IProfile;
import dev.revere.delta.feature.grant.GrantSerializer;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import org.bson.Document;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
public class MongoProfile implements IProfile {

    /**
     * Load a profile
     *
     * @param profile the profile to load
     */
    @Override
    public void loadProfile(Profile profile) {
        Document document = Delta.getInstance().getServiceManager().getService(ProfileService.class).getCollection().find(Filters.eq("uuid", profile.getUuid().toString())).first();
        if (document == null) {
            this.saveProfile(profile);
            return;
        }

        profile.setName(document.getString("name"));
    }

    /**
     * Save a profile
     *
     * @param profile the profile to save
     */
    @Override
    public void saveProfile(Profile profile) {
        Document document = new Document();
        document.put("uuid", profile.getUuid().toString());
        document.put("name", profile.getName());
        document.put("grants", GrantSerializer.serialize(profile.getGrants()));

        Delta.getInstance().getServiceManager().getService(ProfileService.class).getCollection().replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }
}
