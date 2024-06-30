package dev.revere.delta.feature.advancement;

import dev.revere.delta.Delta;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

/**
 * @author Remi
 * @project Delta
 * @date 6/30/2024
 */
public class AdvancementListener implements Listener {

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        Advancement advancement = event.getAdvancement();
        Player player = event.getPlayer();

        AdvancementService advancementService = Delta.getInstance().getServiceManager().getService(AdvancementService.class);

        if (advancementService != null) {
            String advancementKey = advancement.getKey().getKey();

            AdvancementCategory category = advancementService.getAdvancementCategory(advancementKey);
            if (category != null) {
                advancementService.markAdvancementAsCompleted(player, advancementKey);
            }
        }
    }
}
