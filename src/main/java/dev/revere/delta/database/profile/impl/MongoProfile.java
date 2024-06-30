package dev.revere.delta.database.profile.impl;

import com.google.gson.reflect.TypeToken;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.revere.delta.Delta;
import dev.revere.delta.database.profile.IProfile;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.punishment.Punishment;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import org.bson.Document;

import java.lang.reflect.Type;
import java.util.List;

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
        profile.setCoins(document.containsKey("coins") ? document.getInteger("coins") : 0);
        profile.setTagName(document.getString("tag") == null ? "" : document.getString("tag"));
        profile.setLastDailyReward(document.containsKey("lastDailyReward") ? document.getLong("lastDailyReward") : 0);
        loadGrants(profile, document);
        loadPermissions(profile, document);
        loadPunishments(profile, document);
        loadStaffOptions(profile, document);
        loadRedeemedAdvancements(profile, document);
    }

    /**
     * Load the grants of a profile
     *
     * @param profile the profile to load the grants for
     * @param document the document to load the grants from
     */
    private void loadGrants(Profile profile, Document document) {
        String grantsJson = document.getString("grants");
        if (grantsJson != null) {
            Type grantListType = new TypeToken<List<Grant>>() {}.getType();
            List<Grant> grants = Delta.getInstance().getGson().fromJson(grantsJson, grantListType);
            profile.setGrants(grants);
        } else {
            profile.setGrants(null);
        }
    }

    /**
     * Load the punishments of a profile
     *
     * @param profile the profile to load the punishments for
     * @param document the document to load the punishments from
     */
    private void loadPunishments(Profile profile, Document document) {
        String punishmentsJson = document.getString("punishments");
        if (punishmentsJson != null) {
            Type punishmentListType = new TypeToken<List<Punishment>>() {}.getType();
            List<Punishment> punishments = Delta.getInstance().getGson().fromJson(punishmentsJson, punishmentListType);
            profile.setPunishments(punishments);
        }
    }

    /**
     * Load the permissions of a profile
     *
     * @param profile the profile to load the permissions for
     * @param document the document to load the permissions from
     */
    private void loadPermissions(Profile profile, Document document) {
        String permissionsJson = document.getString("permissions");
        if (permissionsJson != null) {
            Type permissionListType = new TypeToken<List<String>>() {}.getType();
            List<String> permissions = Delta.getInstance().getGson().fromJson(permissionsJson, permissionListType);
            profile.setPermissions(permissions);
        }
    }

    /**
     * Load the staff options of a profile
     *
     * @param profile the profile to load the staff options for
     * @param document the document to load the staff options from
     */
    private void loadStaffOptions(Profile profile, Document document) {
        String staffOptionsJson = document.getString("staffOptions");
        if (staffOptionsJson != null) {
            profile.setStaffOptions(Delta.getInstance().getGson().fromJson(staffOptionsJson, profile.getStaffOptions().getClass()));
        }
    }

    /**
     * Load redeemed advancements of a profile
     *
     * @param profile  the profile to load the redeemed advancements for
     * @param document the document to load the redeemed advancements from
     */
    private void loadRedeemedAdvancements(Profile profile, Document document) {
        String redeemedAdvancementsJson = document.getString("redeemedAdvancements");
        if (redeemedAdvancementsJson != null) {
            Type redeemedAdvancementsListType = new TypeToken<List<String>>() {
            }.getType();
            List<String> redeemedAdvancements = Delta.getInstance().getGson().fromJson(redeemedAdvancementsJson, redeemedAdvancementsListType);
            profile.setRedeemedAdvancements(redeemedAdvancements);
        }
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
        document.put("coins", profile.getCoins());
        document.put("tag", profile.getTagName());
        document.put("lastDailyReward", profile.getLastDailyReward());
        document.put("grants", Delta.getInstance().getGson().toJson(profile.getGrants()));
        document.put("permissions", Delta.getInstance().getGson().toJson(profile.getPermissions()));
        document.put("punishments", Delta.getInstance().getGson().toJson(profile.getPunishments()));
        document.put("staffOptions", Delta.getInstance().getGson().toJson(profile.getStaffOptions()));
        document.put("redeemedAdvancements", Delta.getInstance().getGson().toJson(profile.getRedeemedAdvancements()));

        Delta.getInstance().getServiceManager().getService(ProfileService.class).getCollection().replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }
}
