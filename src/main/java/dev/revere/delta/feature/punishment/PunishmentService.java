package dev.revere.delta.feature.punishment;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
     * Execute a punishment.
     *
     * @param punishment the punishment to execute
     */
    public void executePunishment(Punishment punishment) {
        Player target = plugin.getServer().getPlayer(punishment.getTarget());
        String actionMessage = getActionMessage(punishment);

        if (punishment.isSilent()) {
            notifyPlayers("delta.punishments.see.silent", actionMessage);
        } else {
            notifyPlayers("delta.staff", actionMessage);
        }

        if (target != null) {
            applyPunishmentToTarget(target, punishment);
        }

        addPunishment(punishment, punishment.getTarget());
    }

    /**
     * Execute the removal of a punishment.
     *
     * @param punishment the punishment to remove
     * @param silent     if the removal should be silent
     */
    public void executePunishmentRemoval(Punishment punishment, boolean silent) {
        OfflinePlayer target = plugin.getServer().getOfflinePlayer(punishment.getTarget());
        String actionMessage = getRemovalActionMessage(punishment, punishment.getRemovedBy(), punishment.getRemovedReason());

        if (silent) {
            notifyPlayers("delta.punishments.see.silent", actionMessage);
        } else {
            notifyPlayers("delta.staff", actionMessage);
        }

        removePunishment(punishment, punishment.getRemovedBy(), target.getUniqueId(), punishment.getRemovedReason());
    }

    /**
     * Execute the removal of a punishment.
     *
     * @param punishment the punishment to remove
     * @param reason     the reason for removing the punishment
     */
    private String getRemovalActionMessage(Punishment punishment, String removerName, String reason) {
        String action = switch (punishment.getType()) {
            case BAN -> "unbanned";
            case MUTE -> "unmuted";
            case BLACKLIST -> "unblacklisted";
            default -> "removed punishment";
        };

        String message = "&b" + removerName + " &7has " + action + " for &b" + punishment.getTargetName();
        if (!reason.isEmpty()) {
            message += " - Reason: " + reason;
        }

        return CC.translate(message);
    }

    /**
     * Get the action message for a punishment.
     *
     * @param punishment the punishment to get the action message for
     * @return the action message
     */
    private String getActionMessage(Punishment punishment) {
        String action;
        String durationMessage = punishment.isPermanent() ? "permanently" : "for " + punishment.getFormattedDuration();

        switch (punishment.getType()) {
            case BAN -> action = "banned";
            case WARN -> action = "warned";
            case MUTE -> action = "muted";
            case KICK -> {
                action = "kicked";
                durationMessage = "";
            }
            case BLACKLIST -> action = "blacklisted";
            default -> action = "";
        }

        return CC.translate("&b" + punishment.getAddedBy() + " &7has " + action + " &b" + punishment.getTargetName() + " " + durationMessage);
    }

    /**
     * Notify players with a specific permission of a message.
     *
     * @param permission the permission to check for
     * @param message    the message to send
     */
    private void notifyPlayers(String permission, String message) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(message);
            }
        }
    }

    /**
     * Apply a punishment to a target player.
     *
     * @param target     the target player
     * @param punishment the punishment to apply
     */
    private void applyPunishmentToTarget(Player target, Punishment punishment) {
        switch (punishment.getType()) {
            case BAN:
            case BLACKLIST:
                target.kickPlayer(CC.translate("&cYou have been " + punishment.getType().toString().toLowerCase() + " from the server for " + punishment.getReason() + "."));
                break;
            case MUTE:
                target.sendMessage(CC.translate("&cYou have been muted for " + punishment.getReason() + " for " + punishment.getFormattedDuration() + "."));
                break;
            case KICK:
                target.kickPlayer(CC.translate("&cYou have been kicked from the server."));
                break;
            default:
                break;
        }
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
        return profile.getPunishments().stream().anyMatch(punishment -> punishment.getType() == type && !punishment.hasExpired());
    }

    /**
     * Get an active punishment for a profile.
     *
     * @param profile the profile to get the punishment for
     * @param type    the type of punishment to get
     * @return the active punishment, or null if there is none
     */
    public Punishment getActivePunishment(Profile profile, PunishmentType type) {
        return profile.getPunishments().stream().filter(punishment -> punishment.getType() == type && !punishment.hasExpired()).findFirst().orElse(null);
    }
}
