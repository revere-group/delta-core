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
public class CombatLogResetCommand extends BaseCommand {
    @Command(name = "combatlog.reset", permission = "revsentials.combatlog.reset", inGameOnly = false, description = "Reset your combat log")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /combatlog reset <player>"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        Revsential.getInstance().getServiceManager().getService(CombatLogService.class).removeCombatPlayer(target.getUniqueId());
        player.sendMessage(CC.translate("&fCombat log reset for &b" + target.getName()));

    }
}
