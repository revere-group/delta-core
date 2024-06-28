package dev.revere.delta.feature.cooldown;

import dev.revere.delta.util.TaskUtils;
import lombok.Getter;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
@Getter
public class Cooldown {

    private final long cooldownDuration;
    private final Runnable actionToRun;
    private long startTime;
    private BukkitTask cooldownTask;

    /**
     * Constructor for the Cooldown class
     *
     * @param cooldownDuration the duration of the cooldown
     * @param actionToRun      the action to run when the cooldown is over
     */
    public Cooldown(long cooldownDuration, Runnable actionToRun) {
        this.cooldownDuration = cooldownDuration;
        this.actionToRun = actionToRun;
        this.startTime = 0L;
    }

    public long calculateEndTime() {
        return startTime + cooldownDuration;
    }

    public boolean isActive() {
        return calculateEndTime() > System.currentTimeMillis() && cooldownTask != null;
    }

    public Cooldown resetCooldown() {
        startTime = System.currentTimeMillis();
        cancelExistingTask();
        startNewCooldownTask();
        return this;
    }

    public void cancelCooldown() {
        startTime = 0L;
        cancelExistingTask();
    }

    public int remainingTime() {
        return (int) ((calculateEndTime() - System.currentTimeMillis()) / 1000);
    }

    private void cancelExistingTask() {
        if (cooldownTask != null) {
            cooldownTask.cancel();
            cooldownTask = null;
        }
    }

    private void startNewCooldownTask() {
        cooldownTask = TaskUtils.runLaterAsync(() -> {
            if (actionToRun != null) {
                actionToRun.run();
            }
            cancelExistingTask();
        }, cooldownDuration / 50L);
    }
}
