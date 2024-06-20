package dev.revere.revsentials.util;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.color.ColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author Remi
 * @project Revsential
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
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Plugin: &3" + Revsential.getInstance().getName()));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Author: &3" + Revsential.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Version: &3" + Revsential.getInstance().getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Spigot: &3" + Bukkit.getName()));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Load time: &3" + (timeTaken) + " &3ms"));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&8&m-----------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(" ");
    }
}
