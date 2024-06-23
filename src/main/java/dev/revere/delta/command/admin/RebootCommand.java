package dev.revere.delta.command.admin;

import dev.revere.delta.Delta;
import dev.revere.delta.api.command.BaseCommand;
import dev.revere.delta.api.command.annotation.Command;
import dev.revere.delta.api.command.CommandArgs;
import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class RebootCommand extends BaseCommand {
    private boolean undergoingReboot = false;
    private BukkitRunnable countdownTask;

    @Command(name = "reboot", permission = "delta.admin.reboot", description = "Reboots the server", usage = "/reboot <time> | <cancel>")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 1 && args[0].equalsIgnoreCase("cancel")) {
            cancelReboot(player);
            return;
        } else if (args.length == 1) {
            startReboot(player, args);
            return;
        }

        sendUsageMessage(player);
    }

    /**
     * Start the reboot
     *
     * @param player the player who started the reboot
     * @param args   the arguments passed with the command
     */
    private void startReboot(Player player, String[] args) {
        if (undergoingReboot) {
            sendMessage(player, "&cThe server is already undergoing a reboot.");
            return;
        }

        if (args.length != 1) {
            sendUsageMessage(player);
            return;
        }

        int time;
        try {
            time = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sendMessage(player, "&cPlease enter a valid number.");
            return;
        }

        if (time < 1) {
            sendMessage(player, "&cPlease enter a number greater than 0.");
            return;
        }

        undergoingReboot = true;
        startCountdown(time);
    }

    /**
     * Start the countdown
     *
     * @param time the time to count down from
     */
    private void startCountdown(int time) {
        countdownTask = new BukkitRunnable() {
            int seconds = time;

            @Override
            public void run() {
                if (seconds == 0) {
                    Bukkit.broadcastMessage(CC.translate("&aThe server is rebooting..."));
                    Bukkit.shutdown();
                    cancel();
                    undergoingReboot = false;
                    return;
                }

                if (seconds % 60 == 0 || seconds <= 10) {
                    Bukkit.broadcastMessage(CC.translate("&aThe server will reboot in " + seconds + " seconds."));
                }

                seconds--;
            }
        };
        countdownTask.runTaskTimer(Delta.getInstance(), 0L, 20L);
    }

    /**
     * Cancel the reboot
     *
     * @param player the player who canceled the reboot
     */
    private void cancelReboot(Player player) {
        if (!undergoingReboot || countdownTask == null) {
            player.sendMessage(CC.translate("&cThere is no reboot progress to cancel."));
            return;
        }

        countdownTask.cancel();
        undergoingReboot = false;
        Bukkit.broadcastMessage(CC.translate("&aThe reboot was canceled by " + player.getName() + "."));
    }

    /**
     * Send a usage message to the player
     *
     * @param player the player to send the message to
     */
    private void sendUsageMessage(Player player) {
        sendMessage(player, "&cUsage: /reboot <time> | <cancel>");
    }

    /**
     * Send a message to the player
     *
     * @param player  the player to send the message to
     * @param message the message to send
     */
    private void sendMessage(Player player, String message) {
        player.sendMessage(CC.translate(message));
    }
}
