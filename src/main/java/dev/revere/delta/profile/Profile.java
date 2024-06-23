package dev.revere.delta.profile;

import com.mongodb.client.MongoCollection;
import dev.revere.delta.Delta;
import dev.revere.delta.database.profile.IProfile;
import dev.revere.delta.profile.staff.setting.StaffOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
@Getter
@Setter
public class Profile {
    public MongoCollection<Document> collection;

    private final StaffOptions staffOptions;

    private String name;
    private UUID uuid;

    private boolean online;

    /**
     * Constructor for the Profile class
     *
     * @param uuid the UUID of the profile
     */
    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(this.uuid).getName();
        this.staffOptions = new StaffOptions();
    }

    public void loadProfile() {
        Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile().loadProfile(this);
    }

    public void saveProfile() {
        Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile().saveProfile(this);
    }
}
