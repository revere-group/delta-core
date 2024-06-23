package dev.revere.delta.command.player;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/18/2024
 */
public class RulesCommand extends BaseCommand {
    @Command(name = "rules", description = "Display the server rules")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lRules:"));
        player.sendMessage(CC.translate(" &f● &bNo hacking or cheating"));
        player.sendMessage(CC.translate(" &f● &bNo griefing or stealing"));
        player.sendMessage(CC.translate(" &f● &bNo exploiting or x-raying"));
        player.sendMessage(CC.translate(" &f● &bNo spamming or advertising"));
        player.sendMessage(CC.translate(" &f● &bNo abusing bugs or glitches"));
        player.sendMessage(CC.translate(" &f● &bNo lag machines or excessive redstone"));
        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lPunishments:"));
        player.sendMessage(CC.translate(" &f● &b1st Offense: Warning"));
        player.sendMessage(CC.translate(" &f● &b2nd Offense: 1 Day Ban"));
        player.sendMessage(CC.translate(" &f● &b3rd Offense: 7 Day Ban"));
        player.sendMessage(CC.translate(" &f● &b4th Offense: Permanent Ban"));
        player.sendMessage("");
    }
}
