package dev.revere.delta.util;

import dev.revere.delta.Delta;
import dev.revere.delta.api.color.ColorAPI;
import dev.revere.delta.feature.rank.RankService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class CC {

    public static final String MENU_BAR = ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------";

    /**
     * Translates a string with color codes
     *
     * @param text the text to translate
     * @return the translated text
     */
    public static String translate(String text) {
        return ColorAPI.colorize(text);
        //return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Sends a message to the console with information about the plugin
     *
     * @param timeTaken the time taken to load the plugin
     */
    public static void enableMessage(long timeTaken) {
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate("&8&m-----------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Plugin: &3" + Delta.getInstance().getName()));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Author: &3" + Delta.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Version: &3" + Delta.getInstance().getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Spigot: &3" + Bukkit.getName()));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Ranks: &3" + Delta.getInstance().getServiceManager().getService(RankService.class).getRanks().size()));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Load time: &3" + (timeTaken) + " &3ms"));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&8&m-----------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(" ");
    }
}
