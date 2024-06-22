package dev.revere.revsentials.feature.combat.command;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.command.BaseCommand;
import dev.revere.revsentials.api.command.CommandArgs;
import dev.revere.revsentials.api.command.annotation.Command;
import dev.revere.revsentials.feature.combat.CombatLogService;
import dev.revere.revsentials.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/21/2024
 */
public class CombatLogTimeCommand extends BaseCommand {
    @Command(name = "combatlog.time", inGameOnly = true, description = "Check your combat log status")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        long remainingCombatTime = Revsential.getInstance().getServiceManager().getService(CombatLogService.class).getRemainingCombatTime(player);

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
