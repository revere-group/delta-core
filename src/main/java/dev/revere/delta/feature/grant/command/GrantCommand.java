package dev.revere.delta.feature.grant.command;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 22:21
 */
public class GrantCommand extends BaseCommand {
    @Override
    @Command(name = "grant", permission = "delta.command.grant")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate("&cHavent done this yet :)"));
    }
}
