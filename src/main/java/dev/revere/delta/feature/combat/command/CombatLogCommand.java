package dev.revere.delta.feature.combat.command;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/21/2024
 */
public class CombatLogCommand extends BaseCommand {
    @Command(name = "combatlog", inGameOnly = true, description = "Combat log command")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lCombat Log Commands Help:"));
        player.sendMessage(CC.translate(" &f● &b/combatlog status &7| Check your combat log status"));
        player.sendMessage(CC.translate(" &f● &b/combatlog time &7| Check remaining combat time"));
        player.sendMessage("");
    }
}
