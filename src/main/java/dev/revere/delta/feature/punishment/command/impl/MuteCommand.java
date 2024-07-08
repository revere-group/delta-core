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
import dev.revere.delta.util.DateUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
public class MuteCommand extends BaseCommand {
    @Command(name = "mute", inGameOnly = false, description = "Mute a player", permission = "delta.command.mute", usage = "mute <player> <duration> [reason]")
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&cUsage: /mute <player> <duration> [reason]"));
            return;
        }

        String targetName = args[0];
        OfflinePlayer target = Delta.getInstance().getServer().getOfflinePlayer(targetName);
        Profile profile = Delta.getInstance().getServiceManager().getService(ProfileService.class).getProfile(target.getUniqueId());

        String duration = "perm";
        if (args.length > 1 && !args[1].equalsIgnoreCase("perm") && !args[1].equalsIgnoreCase("permanent")) {
            duration = args[1];
        }

        String reason = args.length > 2 ? String.join(" ", Arrays.copyOfRange(args, 2, args.length)) : "No reason provided";

        PunishmentService punishmentService = Delta.getInstance().getServiceManager().getService(PunishmentService.class);
        if (punishmentService.isPunishmentActive(profile, PunishmentType.MUTE)) {
            sender.sendMessage(CC.translate("&cThat player is already muted."));
            return;
        }

        UUID punisher = sender instanceof Player ? ((Player) sender).getUniqueId() : UUID.randomUUID();
        String punisherName = sender instanceof Player ? sender.getName() : "Console";

        Punishment punishment = new Punishment();
        punishment.setType(PunishmentType.MUTE);
        punishment.setTarget(target.getUniqueId());
        punishment.setPunisher(punisher);
        punishment.setPermanent(isPermanent(duration));
        punishment.setActive(true);
        punishment.setSilent(reason.contains("-s") || reason.contains("-silent"));
        punishment.setDuration(isPermanent(duration) ? -1 : DateUtils.parseTime(duration));
        punishment.setAddedAt(System.currentTimeMillis());
        punishment.setReason(reason);
        punishment.setAddedBy(punisherName);
        punishment.setTargetName(targetName);
        punishmentService.executePunishment(punishment);
    }

    /**
     * Check if the duration is permanent
     *
     * @param duration the duration to check
     * @return if the duration is permanent
     */
    private boolean isPermanent(String duration) {
        return duration.equalsIgnoreCase("permanent") || duration.equalsIgnoreCase("perm");
    }
}
