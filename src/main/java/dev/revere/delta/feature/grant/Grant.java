package dev.revere.delta.feature.grant;

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
    private String rank;
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
}
