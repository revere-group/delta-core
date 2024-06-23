/*    */ package dev.revere.delta.util;
/*    */ 
/*    */

import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

/*    */
/*    */ 
/*    */ public class TPSUtils
/*    */ {
/*    */   private static Object minecraftServer;
/*    */   private static Field recentTps;
/* 14 */   private static DecimalFormat SECONDS_FORMAT = new DecimalFormat("#0.0");
/*    */   
/*    */   private static double[] getTps() {
/*    */     try {
/* 18 */       if (minecraftServer == null) {
/* 19 */         Server server = Bukkit.getServer();
/* 20 */         Field consoleField = server.getClass().getDeclaredField("console");
/* 21 */         consoleField.setAccessible(true);
/* 22 */         minecraftServer = consoleField.get(server);
/*    */       } 
/* 24 */       if (recentTps == null) {
/* 25 */         recentTps = minecraftServer.getClass().getSuperclass().getDeclaredField("recentTps");
/* 26 */         recentTps.setAccessible(true);
/*    */       } 
/* 28 */       return (double[])recentTps.get(minecraftServer);
/* 29 */     } catch (IllegalAccessException|NoSuchFieldException illegalAccessException) {
/*    */       
/* 31 */       return new double[] { -1.0D, -1.0D, -1.0D };
/*    */     } 
/*    */   }
/*    */   private static String formatSeconds(long time) {
/* 35 */     return SECONDS_FORMAT.format(((float)time / 1000.0F));
/*    */   }
/*    */ 
/*    */   
/*    */   public static double[] getRecentTps() {
/* 40 */     double[] tps = getTps();
/* 41 */     if (tps[0] >= 20.0D) tps[0] = 20.0D; 
/* 42 */     if (tps[1] >= 20.0D) tps[1] = 20.0D; 
/* 43 */     if (tps[2] >= 20.0D) tps[2] = 20.0D;
/*    */     
/* 45 */     return new double[] { Math.round(tps[0]), Math.round(tps[1]), Math.round(tps[2]) };
/*    */   }
/*    */   
/*    */   public static String getNiceTPS(double tps) {
/* 49 */     if (tps >= 20.0D) return CC.translate("&a" + tps);
/* 50 */     if (tps < 20.0D && tps > 18.0D) return CC.translate("&a" + tps);
/* 51 */     if (tps < 18.0D && tps > 10.0D) return CC.translate("&e" + tps); 
/* 52 */     if (tps < 10.0D && tps > 5.0D) return CC.translate("&c" + tps); 
/* 53 */     return CC.translate("&4" + tps);
/*    */   }
/*    */   
/*    */   public static String getTPSStatus(double tps) {
/* 57 */     if (tps >= 20.0D) return CC.translate("&aExcellent"); 
/* 58 */     if (tps < 20.0D && tps > 18.0D) return CC.translate("&aHigh"); 
/* 59 */     if (tps < 18.0D && tps > 10.0D) return CC.translate("&eMedium"); 
/* 60 */     if (tps < 10.0D && tps > 5.0D) return CC.translate("&cLow"); 
/* 61 */     return CC.translate("&4Risky, lower than 5!!");
/*    */   }
/*    */ }


/* Location:              C:\Users\vifez\Downloads\AquaCore.jar!\me\activated\cor\\utilities\server\TPSUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */