package dev.revere.delta.util;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.rank.RankService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        text = translateHexColors(text);
        text = translateGradients(text);
        text = ChatColor.translateAlternateColorCodes('&', text);

        return text;
    }

    /**
     * Translates hex colors in the format <#FFFFFF>
     *
     * @param text the text to translate
     * @return the translated text
     */
    private static String translateHexColors(String text) {
        Pattern hexPattern = Pattern.compile("<#([A-Fa-f0-9]{6})>");
        Matcher matcher = hexPattern.matcher(text);
        while (matcher.find()) {
            String color = matcher.group(1);
            text = text.replace(matcher.group(), net.md_5.bungee.api.ChatColor.of("#" + color).toString());
        }
        return text;
    }


    /**
     * Translates gradients in the format <gradient:FFFFFF:000000>text</gradient>
     *
     * @param text the text to translate
     * @return the translated text
     */
    private static String translateGradients(String text) {
        Pattern gradientPattern = Pattern.compile("<gradient:([A-Fa-f0-9]{6}):([A-Fa-f0-9]{6})>(.*?)</gradient>");
        Matcher matcher = gradientPattern.matcher(text);
        while (matcher.find()) {
            String startColor = matcher.group(1);
            String endColor = matcher.group(2);
            String gradientText = matcher.group(3);

            String gradient = applyGradient(startColor, endColor, gradientText);
            text = text.replace(matcher.group(), gradient);
        }
        return text;
    }

    /**
     * Applies a gradient to a string
     *
     * @param startColor the start color of the gradient
     * @param endColor the end color of the gradient
     * @param text the text to apply the gradient to
     * @return the text with the gradient applied
     */
    private static String applyGradient(String startColor, String endColor, String text) {
        int length = text.length();

        int[] startRGB = hexToRgb(startColor);
        int[] endRGB = hexToRgb(endColor);

        StringBuilder gradientText = new StringBuilder();

        boolean bold = false;
        boolean italic = false;
        boolean underline = false;
        boolean strikethrough = false;
        boolean magic = false;

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);

            if (c == '&' && i + 1 < length) {
                char formatCode = text.charAt(i + 1);
                switch (formatCode) {
                    case 'l': bold = true; break;
                    case 'o': italic = true; break;
                    case 'n': underline = true; break;
                    case 'm': strikethrough = true; break;
                    case 'k': magic = true; break;
                    case 'r':
                        bold = italic = underline = strikethrough = magic = false;
                        break;
                }
                i++;
                continue;
            }

            double ratio = (double) i / (length - 1);
            int red = (int) (startRGB[0] + ratio * (endRGB[0] - startRGB[0]));
            int green = (int) (startRGB[1] + ratio * (endRGB[1] - startRGB[1]));
            int blue = (int) (startRGB[2] + ratio * (endRGB[2] - startRGB[2]));

            String hex = String.format("#%02x%02x%02x", red, green, blue);
            gradientText.append(net.md_5.bungee.api.ChatColor.of(hex));

            if (bold) gradientText.append(ChatColor.BOLD);
            if (italic) gradientText.append(ChatColor.ITALIC);
            if (underline) gradientText.append(ChatColor.UNDERLINE);
            if (strikethrough) gradientText.append(ChatColor.STRIKETHROUGH);
            if (magic) gradientText.append(ChatColor.MAGIC);

            gradientText.append(c);
        }

        return gradientText.toString();
    }

    /**
     * Converts a hex string to an RGB array
     *
     * @param hex the hex string to convert
     * @return the RGB array
     */
    private static int[] hexToRgb(String hex) {
        return new int[]{
                Integer.valueOf(hex.substring(0, 2), 16),
                Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16)
        };
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
