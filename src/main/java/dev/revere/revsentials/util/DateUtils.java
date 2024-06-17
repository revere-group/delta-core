package dev.revere.revsentials.util;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class DateUtils {

    /**
     * Formats a time in milliseconds to a readable format.
     *
     * @param millis the time in milliseconds
     * @return the formatted time
     */
    public static String formatTimeMillis(long millis) {
        long seconds = millis / 1000L;

        if (seconds <= 0L) {
            return "0 seconds";
        }

        long minutes = seconds / 60L;
        seconds %= 60L;
        long hours = minutes / 60L;
        minutes %= 60L;
        long days = hours / 24L;
        hours %= 24L;
        long years = days / 365L;
        days %= 365L;

        StringBuilder time = new StringBuilder();

        if (years != 0L) {
            time.append(years).append((years == 1L) ? " year " : " years ");
        }

        if (days != 0L) {
            time.append(days).append((days == 1L) ? " day " : " days ");
        }

        if (hours != 0L) {
            time.append(hours).append((hours == 1L) ? " hour " : " hours ");
        }

        if (minutes != 0L) {
            time.append(minutes).append((minutes == 1L) ? " minute " : " minutes ");
        }

        if (seconds != 0L) {
            time.append(seconds).append((seconds == 1L) ? " second " : " seconds ");
        }

        return time.toString().trim();
    }


}