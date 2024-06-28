package dev.revere.delta.feature.grant;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.util.DateUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 21:18
 */
@Getter
@Setter
public class Grant {
    private String rankName;
    private String server;
    private String reason;
    private String addedBy;
    private String removedBy;
    private String removedReason;
    private long addedAt;
    private long removedAt;
    private long duration;
    private boolean active;
    private boolean permanent;

    /**
     * Get the rank of the grant
     *
     * @return the rank of the grant
     */
    public Rank getRank() {
        Rank rank = Delta.getInstance().getServiceManager().getService(RankService.class).getRank(rankName);
        if (rank != null && rank.isAccessibleOnServer()) {
            return rank;
        }

        return null;
    }

    /**
     * Get the rank information
     *
     * @return the rank information
     */
    public Rank getRankInformation() {
        return Delta.getInstance().getServiceManager().getService(RankService.class).getRank(rankName);
    }

    /**
     * Check if the grant has expired
     *
     * @return true if the grant has expired, false otherwise
     */
    public boolean hasExpired() {
        return !permanent && System.currentTimeMillis() >= addedAt + duration;
    }

    /**
     * Get the expiration of the grant
     *
     * @return the expiration of the grant
     */
    public String getExpiration() {
        if (permanent) {
            return "Permanent";
        }
        long time = addedAt + duration - System.currentTimeMillis();
        long days = time / 86400000;
        long hours = (time % 86400000) / 3600000;
        long minutes = (time % 3600000) / 60000;
        long seconds = (time % 60000) / 1000;
        return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
    }

    /**
     * Get the expiration date of the grant
     *
     * @return the expiration date of the grant
     */
    public String getExpirationDate() {
        return DateUtils.formatDate(addedAt + duration);
    }
}
