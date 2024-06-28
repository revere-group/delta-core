package dev.revere.delta.feature.punishment;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
@Getter
public class PunishmentService implements IService {

    private final Delta plugin;

    /**
     * Constructor for the PunishmentService class
     *
     * @param plugin the main class of the plugin
     */
    public PunishmentService(Delta plugin) {
        this.plugin = plugin;
    }


    @Override
    public void register() {

    }

    /**
     * Add a punishment to a profile.
     *
     * @param punishment the punishment to add
     * @param uuid       the UUID of the profile
     */
    public void addPunishment(Punishment punishment, UUID uuid) {
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(uuid);

        List<Punishment> punishments = profile.getPunishments();
        punishments = punishments == null ? new ArrayList<>() : new ArrayList<>(punishments);
        punishments.add(punishment);

        profile.setPunishments(punishments);
        profile.saveProfile();
    }

    /**
     * Remove a punishment from a profile.
     *
     * @param punishment the punishment to remove
     * @param player     the player removing the punishment
     * @param targetId   the UUID of the profile
     * @param reason     the reason for removing the punishment
     */
    public void removePunishment(Punishment punishment, String player, UUID targetId, String reason) {
        ProfileService profileService = Delta.getInstance().getServiceManager().getService(ProfileService.class);
        Profile profile = profileService.getProfile(targetId);

        punishment.setRemovedBy(player);
        punishment.setRemovedAt(System.currentTimeMillis());
        punishment.setRemovedReason(reason);
        punishment.setActive(false);

        profile.saveProfile();
    }

    /**
     * Check if a punishment is active for a profile.
     *
     * @param profile the profile to check
     * @param type    the type of punishment to check for
     * @return true if the punishment is active, false otherwise
     */
    public boolean isPunishmentActive(Profile profile, PunishmentType type) {
        if (profile == null || profile.getPunishments() == null) {
            return false;
        }
        return profile.getPunishments().stream().anyMatch(punishment -> punishment.getType() == type && punishment.isActive());
    }

    /**
     * Get an active punishment for a profile.
     *
     * @param profile the profile to get the punishment for
     * @param type    the type of punishment to get
     * @return the active punishment, or null if there is none
     */
    public Punishment getActivePunishment(Profile profile, PunishmentType type) {
        return profile.getPunishments().stream().filter(punishment -> punishment.getType() == type && punishment.isActive()).findFirst().orElse(null);
    }

    /**
     * Get all active grants for a profile.
     *
     * @param profile the profile to get the grants for
     * @return a list of active grants
     */
    public List<Punishment> getActivePunishments(Profile profile) {
        return profile.getPunishments().stream().filter(punishment -> !punishment.hasExpired() && punishment.isActive()).toList();
    }
}
