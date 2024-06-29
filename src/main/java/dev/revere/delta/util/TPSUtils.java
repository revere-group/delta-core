package dev.revere.delta.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

/**
 * @author Emmy
 * @project Delta
 * @date 29/06/2024 - 08:31
 * @credit: Activated
 */
@UtilityClass
public class TPSUtils {
    private Object minecraftServer;
    private Field recentTps;
    private final DecimalFormat SECONDS_FORMAT = new DecimalFormat("#0.0");

    private double[] getTps() {
        try {
            if (minecraftServer == null) {
                Server server = Bukkit.getServer();
                Field consoleField = server.getClass().getDeclaredField("console");
                consoleField.setAccessible(true);
                minecraftServer = consoleField.get(server);
            }
            if (recentTps == null) {
                recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
                recentTps.setAccessible(true);
            }
            return (double[]) recentTps.get(minecraftServer);
        } catch (IllegalAccessException | NoSuchFieldException illegalAccessException) {

            return new double[]{-1.0D, -1.0D, -1.0D};
        }
    }

    private String formatSeconds(long time) {
        return SECONDS_FORMAT.format(((float) time / 1000.0F));
    }

    public double[] getRecentTps() {
        double[] tps = getTps();
        if (tps[0] >= 20.0D) tps[0] = 20.0D;
        if (tps[1] >= 20.0D) tps[1] = 20.0D;
        if (tps[2] >= 20.0D) tps[2] = 20.0D;

        return new double[]{Math.round(tps[0]), Math.round(tps[1]), Math.round(tps[2])};
    }

    public String getNiceTPS(double tps) {
        if (tps >= 20.0D) return CC.translate("&a" + tps);
        if (tps < 20.0D && tps > 18.0D) return CC.translate("&a" + tps);
        if (tps < 18.0D && tps > 10.0D) return CC.translate("&e" + tps);
        if (tps < 10.0D && tps > 5.0D) return CC.translate("&c" + tps);
        return CC.translate("&4" + tps);
    }

    public String getTPSStatus(double tps) {
        if (tps >= 20.0D) return CC.translate("&aExcellent");
        if (tps < 20.0D && tps > 18.0D) return CC.translate("&aHigh");
        if (tps < 18.0D && tps > 10.0D) return CC.translate("&eMedium");
        if (tps < 10.0D && tps > 5.0D) return CC.translate("&cLow");
        return CC.translate("&4Risky, lower than 5!!");
    }
}
