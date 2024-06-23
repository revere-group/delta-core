package dev.revere.delta.feature.combat;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.feature.combat.listener.CombatLogListener;
import dev.revere.delta.util.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Remi
 * @project Delta
 * @date 6/21/2024
 */
@Getter
public class CombatLogService implements IService {
    private final Map<UUID, Long> combatLog = new HashMap<>();
    private final Delta plugin;
    private final long combatDuration = TimeUnit.SECONDS.toMillis(30);

    /**
     * Constructor for the CombatLogService class
     *
     * @param plugin the main class of the plugin
     */
    public CombatLogService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        Bukkit.getPluginManager().registerEvents(new CombatLogListener(this), plugin);
    }

    /**
     * Adds a player to combat log or resets their timer if they are already in combat.
     *
     * @param player the player to be put into combat
     */
    public void addPlayerToCombat(Player player) {
        UUID uuid = player.getUniqueId();
        if (!combatLog.containsKey(uuid)) {
            player.sendMessage(CC.translate("&cYou are now in combat."));
        }
        combatLog.put(uuid, System.currentTimeMillis() + combatDuration);
    }

    /**
     * Removes a player from combat log.
     *
     * @param uuid the UUID of the player to remove
     */
    public void removeCombatPlayer(UUID uuid) {
        if (combatLog.containsKey(uuid)) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(CC.translate("&cYou are no longer in combat."));
            }
            combatLog.remove(uuid);
        }
    }

    /**
     * Checks if a player is currently combat logged.
     *
     * @param player the player to check
     * @return true if the player is combat logged, false otherwise
     */
    public boolean isPlayerInCombat(Player player) {
        UUID uuid = player.getUniqueId();
        if (!combatLog.containsKey(uuid)) {
            return false;
        }
        if (combatLog.get(uuid) < System.currentTimeMillis()) {
            combatLog.remove(uuid);
            player.sendMessage(CC.translate("&cYou are no longer in combat."));
            return false;
        }
        return true;
    }

    /**
     * Gets the remaining combat time for a player.
     *
     * @param player the player to check
     * @return the remaining time in seconds, or 0 if not in combat
     */
    public long getRemainingCombatTime(Player player) {
        UUID uuid = player.getUniqueId();
        if (!combatLog.containsKey(uuid)) {
            return 0;
        }
        long remainingTime = combatLog.get(uuid) - System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toSeconds(Math.max(remainingTime, 0));
    }
}
