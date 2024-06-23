package dev.revere.delta.profile;

import com.mongodb.client.MongoCollection;
import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.database.MongoService;
import dev.revere.delta.database.profile.IProfile;
import dev.revere.delta.database.profile.impl.MongoProfile;
import dev.revere.delta.profile.listener.ProfileListener;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
@Setter
@Getter
public class ProfileService implements IService {

    private final Map<UUID, Profile> profiles = new HashMap<>();
    public MongoCollection<Document> collection;
    public final IProfile profile;

    private final Delta plugin;

    /**
     * Constructor for the ProfileService class
     *
     * @param plugin the main class of the plugin
     */
    public ProfileService(Delta plugin) {
        this.plugin = plugin;
        this.profile = new MongoProfile();
    }

    @Override
    public void register() {
        this.collection = Delta.getInstance().getServiceManager().getService(MongoService.class).getDatabase().getCollection("profiles");
        this.collection.find().forEach(this::loadProfile);

        Bukkit.getPluginManager().registerEvents(new ProfileListener(), plugin);
    }

    /**
     * Load a profile from a document
     *
     * @param document the document to load the profile from
     */
    private void loadProfile(Document document) {
        UUID uuid = UUID.fromString(document.getString("uuid"));
        Profile profile = new Profile(uuid);
        profile.loadProfile();

        profiles.put(profile.getUuid(), profile);
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
