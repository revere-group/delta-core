package dev.revere.delta.feature.combat.command;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.combat.CombatLogService;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/21/2024
 */
public class CombatLogTimeCommand extends BaseCommand {
    @Command(name = "combatlog.time", inGameOnly = true, description = "Check your combat log status")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        long remainingCombatTime = Delta.getInstance().getServiceManager().getService(CombatLogService.class).getRemainingCombatTime(player);

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lCombat Log Time:"));
        if (remainingCombatTime == 0) {
            player.sendMessage(CC.translate(" &f● &fYou are currently &anot in combat"));
            player.sendMessage("");
            return;
        }
        player.sendMessage(CC.translate(" &f● &fYou have &b" + remainingCombatTime + " &fseconds remaining"));
    }
}
