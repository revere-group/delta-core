package dev.revere.delta.profile;

import com.mongodb.client.MongoCollection;
import dev.revere.delta.Delta;
import dev.revere.delta.database.profile.IProfile;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.punishment.Punishment;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.staff.setting.StaffOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
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

    private StaffOptions staffOptions;

    private List<Punishment> punishments;
    private List<Grant> grants;
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
        this.grants = new ArrayList<>();
        this.punishments = new ArrayList<>();
    }

    /**
     * Get the name color of the player
     *
     * @return the name color of the player
     */
    public String getNameColor() {
        RankService rankService = Delta.getInstance().getServiceManager().getService(RankService.class);
        Rank rank = rankService.getHighestRank(this);
        if (rank == null) {
            Rank defaultRank = rankService.getDefaultRank();
            return defaultRank.getNameColor().toString();
        }

        if (rank.getNameColor() == null) {
            return ChatColor.GRAY.toString();
        }

        return rank.getNameColor().toString();
    }

    public void loadProfile() {
        Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile().loadProfile(this);
    }

    public void saveProfile() {
        Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile().saveProfile(this);
    }
}
