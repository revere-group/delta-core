package dev.revere.delta.profile;

import com.mongodb.client.MongoCollection;
import dev.revere.delta.Delta;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.punishment.Punishment;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.feature.staff.setting.StaffOptions;
import dev.revere.delta.feature.tag.Tag;
import dev.revere.delta.feature.tag.TagService;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private List<String> permissions;
    private List<Grant> grants;
    private String tagName;
    private String name;
    private UUID uuid;

    private List<String> redeemedAdvancements;
    private long lastDailyReward;

    private boolean online;
    private int coins;

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
        this.permissions = new ArrayList<>();
        this.redeemedAdvancements = new ArrayList<>();
        this.lastDailyReward = 0;
        this.coins = 0;
        this.tagName = "";
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

    /**
     * Get the tag of the player
     *
     * @return the tag of the player
     */
    public Tag getTag() {
        return Delta.getInstance().getServiceManager().getService(TagService.class).getTag(this.tagName);
    }

    /**
     * Check if the player has redeemed a specific advancement
     *
     * @param advancementKey The key of the advancement to check
     * @return true if the advancement has been redeemed, false otherwise
     */
    public boolean hasAdvancementRedeemed(String advancementKey) {
        return redeemedAdvancements.contains(advancementKey);
    }

    /**
     * Mark an advancement as redeemed for the player
     *
     * @param advancementKey The key of the advancement to mark as redeemed
     */
    public void markAdvancementAsRedeemed(String advancementKey) {
        if (!redeemedAdvancements.contains(advancementKey)) {
            redeemedAdvancements.add(advancementKey);
            saveProfile();
        }
    }

    /**
     * Get the remaining time until the player can claim their daily reward
     *
     * @return the remaining time until the player can claim their daily reward
     */
    public String getDailyRewardRemainingTime() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastClaim = currentTime - lastDailyReward;
        long timeRemaining = 86400000 - timeSinceLastClaim;

        if (timeRemaining <= 0) {
            return "&aYou can claim your reward now!";
        }

        long hoursRemaining = TimeUnit.MILLISECONDS.toHours(timeRemaining);
        long minutesRemaining = TimeUnit.MILLISECONDS.toMinutes(timeRemaining) % 60;
        long secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(timeRemaining) % 60;

        return String.format("%dh, %dm, and %ds", hoursRemaining, minutesRemaining, secondsRemaining);
    }

    /**
     * Check if the player can claim their daily reward
     *
     * @return if the player can claim their daily reward
     */
    public boolean isDailyRewardClaimable() {
        return lastDailyReward == 0 || System.currentTimeMillis() - lastDailyReward >= 86400000;
    }

    public void loadProfile() {
        Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile().loadProfile(this);
    }

    public void saveProfile() {
        Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile().saveProfile(this);
    }
}
