package dev.revere.delta.feature.clan.command.impl;

import dev.revere.delta.Delta;
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
public class ClanDeleteCommand extends BaseCommand {
    @Command(name = "clan.delete", aliases = {"clan.disband"}, inGameOnly = true, description = "Delete a clan")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Delta.getInstance().getClanRepository().getPlayerClan(player.getUniqueId()) == null) {
            player.sendMessage(CC.translate("&cYou are not in a clan."));
            return;
        }

        Delta.getInstance().getClanRepository().deleteClan(player);
    }
}
