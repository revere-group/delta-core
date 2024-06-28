package dev.revere.delta.feature.tpa;

import lombok.Getter;

import java.util.UUID;

/**
 * @author Remi
 * @project Delta
 * @date 6/28/2024
 */
@Getter
public class TPARequest {

    private final UUID sender;
    private final UUID target;

    /**
     * Constructor for the TPARequest class
     *
     * @param sender the UUID of the player sending the request
     * @param target the UUID of the player receiving the request
     */
    public TPARequest(UUID sender, UUID target) {
        this.sender = sender;
        this.target = target;
    }
}
