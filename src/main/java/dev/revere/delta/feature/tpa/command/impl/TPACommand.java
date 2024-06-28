package dev.revere.delta.feature.tpa.command.impl;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.cooldown.Cooldown;
import dev.revere.delta.feature.cooldown.CooldownService;
import dev.revere.delta.feature.tpa.TPAService;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Remi
 * @project Delta
 * @date 6/28/2024
 */
public class TPACommand extends BaseCommand {

    private final TPAService tpaService = Delta.getInstance().getServiceManager().getService(TPAService.class);

    @Command(name = "tpa", aliases = {"teleportrequest", "tpask"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /tpa <player> | accept <player> | deny <player> | cancel <player>"));
            return;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "accept":
                acceptTPA(player, args);
                break;
            case "deny":
                denyTPA(player, args);
                break;
            case "cancel":
                cancelTPA(player, args);
                break;
            default:
                sendTPARequest(player, subCommand);
                break;
        }
    }

    /**
     * Send a teleport request to a specific player
     *
     * @param sender     the player sending the request
     * @param targetName the name of the player receiving the request
     */
    private void sendTPARequest(Player sender, String targetName) {
        Player target = Bukkit.getPlayer(targetName);

        CooldownService cooldownService = Delta.getInstance().getServiceManager().getService(CooldownService.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(sender.getUniqueId(), "TPA_COMMAND"));

        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            sender.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before sending another teleport request."));
            return;
        }

        if (target == null || !target.isOnline()) {
            sender.sendMessage(CC.translate("&cPlayer " + targetName + " not found or not online."));
            return;
        }

        if (sender.equals(target)) {
            sender.sendMessage(CC.translate("&cYou cannot teleport to yourself."));
            return;
        }

        tpaService.sendTPARequest(sender, target);
    }

    /**
     * Accept a teleport request from a specific player
     *
     * @param player the player accepting the request
     * @param args   the command arguments
     */
    private void acceptTPA(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /tpa accept <player>"));
            return;
        }

        String targetName = args[1];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            player.sendMessage(CC.translate("&cPlayer " + targetName + " not found or not online."));
            return;
        }

        tpaService.acceptTPARequest(player, target);
    }

    /**
     * Deny a teleport request from a specific player
     *
     * @param player the player denying the request
     * @param args   the command arguments
     */
    private void denyTPA(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /tpa deny <player>"));
            return;
        }

        String targetName = args[1];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            player.sendMessage(CC.translate("&cPlayer " + targetName + " not found or not online."));
            return;
        }

        tpaService.denyTPARequest(player, target);
    }

    /**
     * Cancel a teleport request to a specific player
     *
     * @param player the player cancelling the request
     * @param args   the command arguments
     */
    private void cancelTPA(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /tpa cancel <player>"));
            return;
        }

        String targetName = args[1];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            player.sendMessage(CC.translate("&cPlayer " + targetName + " not found or not online."));
            return;
        }

        tpaService.cancelTPARequest(player, target);
    }
}
