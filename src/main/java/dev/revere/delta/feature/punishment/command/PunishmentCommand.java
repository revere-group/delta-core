package dev.revere.delta.feature.punishment.command;

import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.util.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class PunishmentCommand extends BaseCommand {
    @Command(name = "punishment", inGameOnly = true, description = "Main punishment command")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&b&lPunishment Commands Help:"));
        player.sendMessage(CC.translate(" &f● &b/ban &8(&7name&8) &8(&7duration&8) &8[&7reason&8] &7| Ban a player"));
        player.sendMessage(CC.translate(" &f● &b/unban &8(&7name&8) &7| Unban a player"));
        player.sendMessage(CC.translate(" &f● &b/mute &8(&7name&8) &8(&7duration&8) &8[&7reason&8] &7| Mute a player"));
        player.sendMessage(CC.translate(" &f● &b/unmute &8(&7name&8) &7| Unmute a player"));
        player.sendMessage(CC.translate(" &f● &b/warn &8(&7name&8) &8[&7reason&8] &7| Warn a player"));
        player.sendMessage(CC.translate(" &f● &b/kick &8(&7name&8) &8[&7reason&8] &7| Kick a player"));
        player.sendMessage(CC.translate(" &f● &b/blacklist &8(&7name&8) &7| Blacklist a player"));
        player.sendMessage(CC.translate(" &f● &b/unblacklist &8(&7name&8) &7| Unblacklist a player"));
        player.sendMessage("");
    }
}
