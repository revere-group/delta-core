package dev.revere.delta.feature.combat.listener;

import dev.revere.delta.feature.combat.CombatLogService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author Remi
 * @project Delta
 * @date 6/21/2024
 */
public class CombatLogListener implements Listener {

    private final CombatLogService combatLogService;

    /**
     * Constructor for the CombatLogListener class
     *
     * @param combatLogService the combat log service
     */
    public CombatLogListener(CombatLogService combatLogService) {
        this.combatLogService = combatLogService;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damaged)) {
            return;
        }

        if (event.getDamager() instanceof Player attacker) {
            combatLogService.addPlayerToCombat(attacker);
        }

        combatLogService.addPlayerToCombat(damaged);
    }
}
