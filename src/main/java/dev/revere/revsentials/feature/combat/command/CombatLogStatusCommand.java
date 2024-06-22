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
public class CombatLogStatusCommand extends BaseCommand {
    @Command(name = "combatlog.status", inGameOnly = true, description = "Check your combat log status")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        boolean inCombat = Revsential.getInstance().getServiceManager().getService(CombatLogService.class).isPlayerInCombat(player);

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lCombat Log Status:"));
        player.sendMessage(CC.translate(" &f● &fYou are currently " + (inCombat ? "&cin combat" : "&anot in combat")));
        player.sendMessage("");
    }
}
