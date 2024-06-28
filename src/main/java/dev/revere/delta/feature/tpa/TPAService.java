package dev.revere.delta.feature.tpa;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.cooldown.Cooldown;
import dev.revere.delta.feature.cooldown.CooldownService;
import dev.revere.delta.util.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Remi
 * @project Delta
 * @date 6/28/2024
 */
@Getter
public class TPAService implements IService {

    private final Delta plugin;
    private final Map<UUID, Map<UUID, TPARequest>> tpaRequests;

    /**
     * Constructor for the TPAService class
     *
     * @param plugin the main class of the plugin
     */
    public TPAService(Delta plugin) {
        this.plugin = plugin;
        this.tpaRequests = new HashMap<>();;
    }

    @Override
    public void register() {

    }

    /**
     * Send a teleport request to a player
     *
     * @param sender the player sending the request
     * @param target the player receiving the request
     */
    public void sendTPARequest(Player sender, Player target) {
        UUID targetUUID = target.getUniqueId();
        UUID senderUUID = sender.getUniqueId();

        if (hasPendingRequest(sender, target)) {
            sender.sendMessage(CC.translate("&cYou already have a pending teleport request to " + target.getName() + "."));
            return;
        }

        tpaRequests.putIfAbsent(targetUUID, new HashMap<>());
        tpaRequests.get(targetUUID).put(senderUUID, new TPARequest(senderUUID, targetUUID));

        startRequestTimer(senderUUID, targetUUID);

        target.sendMessage(CC.translate("&fYou have received a teleport request from &b" + sender.getName() + "&f. Type &b/tpa accept " + sender.getName() + " &fto accept."));
        sender.sendMessage(CC.translate("&fTeleport request sent to &b" + target.getName()));
    }

    /**
     * Accept a teleport request from a specific player
     *
     * @param target the player who is accepting the request
     * @param sender the player who sent the request
     */
    public void acceptTPARequest(Player target, Player sender) {
        UUID targetUUID = target.getUniqueId();
        UUID senderUUID = sender.getUniqueId();

        Map<UUID, TPARequest> requests = tpaRequests.get(targetUUID);
        if (requests != null) {
            TPARequest request = requests.get(senderUUID);
            if (request != null) {

                CooldownService cooldownService = Delta.getInstance().getServiceManager().getService(CooldownService.class);
                Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(sender.getUniqueId(), "TPA_COMMAND"));

                sender.teleport(target.getLocation());
                sender.sendMessage(CC.translate("&aYour teleport request to " + target.getName() + " has been accepted."));
                target.sendMessage(CC.translate("&aYou accepted " + sender.getName() + "'s teleport request."));
                requests.remove(senderUUID);

                Cooldown cooldown = optionalCooldown.orElseGet(() -> {
                    Cooldown newCooldown = new Cooldown(360 * 1000L, null);
                    cooldownService.addCooldown(sender.getUniqueId(), "TPA_COMMAND", newCooldown);
                    return newCooldown;
                });

                cooldown.resetCooldown();
            } else {
                target.sendMessage(CC.translate("&cThere are no pending teleport request from " + sender.getName() + " to you."));
            }
        } else {
            target.sendMessage(CC.translate("&cThere are no pending teleport request from " + sender.getName() + " to you."));
        }
    }

    /**
     * Deny a teleport request from a specific player
     *
     * @param target the player who is denying the request
     * @param sender the player who sent the request
     */
    public void denyTPARequest(Player target, Player sender) {
        UUID targetUUID = target.getUniqueId();
        UUID senderUUID = sender.getUniqueId();

        Map<UUID, TPARequest> requests = tpaRequests.get(targetUUID);
        if (requests != null) {
            TPARequest request = requests.get(senderUUID);
            if (request != null) {
                sender.sendMessage(CC.translate("&cYour teleport request to " + target.getName() + " has been denied."));
                target.sendMessage(CC.translate("&cYou denied " + sender.getName() + "'s teleport request."));
                requests.remove(senderUUID);
            } else {
                target.sendMessage(CC.translate("&cThere are no pending teleport request from " + sender.getName() + " to you."));
            }
        } else {
            target.sendMessage(CC.translate("&cThere are no pending teleport request from " + sender.getName() + " to you."));
        }
    }

    /**
     * Cancel a teleport request sent by the player
     *
     * @param sender the player who wants to cancel their request
     * @param target the player to whom the request was sent
     */
    public void cancelTPARequest(Player sender, Player target) {
        UUID targetUUID = target.getUniqueId();

        // Get the map of sender's requests for the target
        Map<UUID, TPARequest> requests = tpaRequests.get(targetUUID);
        if (requests != null) {
            TPARequest request = requests.get(sender.getUniqueId());
            if (request != null) {
                requests.remove(sender.getUniqueId());
                sender.sendMessage("Your teleport request to " + target.getName() + " has been cancelled.");
            } else {
                sender.sendMessage("You have no pending teleport request to " + target.getName() + ".");
            }
        } else {
            sender.sendMessage("You have no pending teleport request to " + target.getName() + ".");
        }
    }

    /**
     * Start a timer to cancel the request after 60 seconds
     *
     * @param senderUUID the UUID of the sender who initiated the request
     * @param targetUUID the UUID of the target who received the request
     */
    private void startRequestTimer(UUID senderUUID, UUID targetUUID) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Map<UUID, TPARequest> requests = tpaRequests.get(targetUUID);
            if (requests != null) {
                requests.remove(senderUUID);
            }
        }, 20 * 60);
    }

    /**
     * Check if the sender already has a pending teleport request to the target.
     *
     * @param sender the player sending the request
     * @param target the player receiving the request
     * @return true if there is a pending request, false otherwise
     */
    private boolean hasPendingRequest(Player sender, Player target) {
        UUID targetUUID = target.getUniqueId();
        UUID senderUUID = sender.getUniqueId();

        Map<UUID, Map<UUID, TPARequest>> requests = getTpaRequests();
        if (requests.containsKey(targetUUID)) {
            Map<UUID, TPARequest> senderRequests = requests.get(targetUUID);
            return senderRequests.containsKey(senderUUID);
        }

        return false;
    }
}
