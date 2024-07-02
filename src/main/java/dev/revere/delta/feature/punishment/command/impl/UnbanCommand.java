package dev.revere.delta.feature.punishment.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.feature.punishment.Punishment;
import dev.revere.delta.feature.punishment.PunishmentService;
import dev.revere.delta.feature.punishment.PunishmentType;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.util.CC;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
public class UnbanCommand extends BaseCommand {
    @Command(name = "unban", inGameOnly = false, description = "Unban a player", permission = "delta.command.unban", usage = "unban <player> [reason]")
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: /unban <player> [reason] [-s]"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer target = sender.getServer().getOfflinePlayer(targetName);
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(target.getUniqueId());
        String reason = args.length > 1 ? String.join(" ", args) : "No reason provided";

        PunishmentService punishmentService = Delta.getInstance().getServiceManager().getService(PunishmentService.class);
        if (!punishmentService.isPunishmentActive(profile, PunishmentType.BAN)) {
            sender.sendMessage(CC.translate("&cThat player is not banned."));
            return;
        }

        Punishment punishment = punishmentService.getActivePunishment(profile, PunishmentType.BAN);
        punishment.setRemovedReason(reason);
        punishment.setRemovedBy(sender instanceof Player ? sender.getName() : "Console");
        punishmentService.executePunishmentRemoval(punishment, reason.contains("-s") || reason.contains("-silent"));
    }
}
