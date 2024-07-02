package dev.revere.delta.feature.punishment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
@Getter
@Setter
@NoArgsConstructor
public class Punishment {

    private PunishmentType type;

    private UUID target;
    private UUID punisher;

    private boolean permanent;
    private boolean active = true;

    private long duration;
    private long addedAt;
    private long removedAt;

    private String reason;
    private String removedReason;

    private String addedBy;
    private String removedBy;
    private String targetName;

    /**
     * Check to see if the punishment has expired
     *
     * @return if the punishment has expired
     */
    public boolean hasExpired() {
        if (!isActive()) return true;
        if (isPermanent()) return false;

        return System.currentTimeMillis() >= addedAt + duration;
    }

    /**
     * Get the remaining time of the punishment
     *
     * @return the remaining time
     */
    public long getRemainingTime() {
        return addedAt + duration - System.currentTimeMillis();
    }
}
