package dev.revere.delta.feature.chat.filter;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
@Getter
@Setter
public class FilterService implements IService {

    private Pattern URL_REGEX = Pattern.compile("^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$");
    private Pattern IP_REGEX = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.,])){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    private final Delta plugin;

    /**
     * Constructor for the FilterService class
     *
     * @param plugin the main class of the plugin
     */
    public FilterService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {

    }

    /**
     * Check if a player can send a message.
     *
     * @param player  the player to check
     * @param message the message to check
     * @return true if the player can send the message, false otherwise
     */
    public boolean runCheck(Player player, String message) {
        if (player.hasPermission("delta.bypass.chat.filter")) {
            return false;
        }
        ConfigService configService = plugin.getServiceManager().getService(ConfigService.class);
        if (!configService.getConfig("messages.yml").getBoolean("chat.filter.enabled")) {
            return false;
        }

        if (isFiltered(message)) {
            List<String> filteredWords = getFilteredWords(message);
            player.sendMessage(CC.translate(configService.getConfig("messages.yml").getString("chat.filter.notify-self").replace("%word%", String.join(", ", filteredWords))));
            notifyStaff(player, filteredWords);
            return true;
        }
        return false;
    }

    /**
     * Check if a message is filtered.
     *
     * @param message the message to check
     * @return true if the message is filtered, false otherwise
     */
    public boolean isFiltered(String message) {
        ConfigService configService = plugin.getServiceManager().getService(ConfigService.class);
        List<String> filteredWords = configService.getConfig("messages.yml").getStringList("chat.filter.words");

        String normalizedMessage = normalizeMessage(message);

        for (String filteredWord : filteredWords) {
            if (normalizedMessage.contains(filteredWord.toLowerCase())) {
                return true;
            }
        }

        String[] rawWords = message
                .replace("(dot)", ".")
                .replace("(.)", ".")
                .replace("/./", ".")
                .replace("[dot]", ".").trim().split("\\s+");

        for (String word : rawWords) {
            if (isUrlOrIp(word)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Normalize a message.
     *
     * @param message the message to normalize
     * @return the normalized message
     */
    private String normalizeMessage(String message) {
        return message.toLowerCase()
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("@", "a")
                .replace("7", "t")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .replaceAll("\\p{Punct}|\\d", "")
                .trim();
    }

    /**
     * Check if a word is a URL or IP.
     *
     * @param word the word to check
     * @return true if the word is a URL or IP, false otherwise
     */
    private boolean isUrlOrIp(String word) {
        Matcher ipMatcher = IP_REGEX.matcher(word);
        if (ipMatcher.matches()) {
            return true;
        }

        Matcher urlMatcher = URL_REGEX.matcher(word);
        return urlMatcher.matches();
    }

    /**
     * Get the filtered words in a message.
     *
     * @param message the message to get the filtered words from
     * @return a list of filtered words
     */
    private List<String> getFilteredWords(String message) {
        ConfigService configService = plugin.getServiceManager().getService(ConfigService.class);
        List<String> filteredWords = configService.getConfig("messages.yml").getStringList("chat.filter.words");

        String normalizedMessage = normalizeMessage(message);

        List<String> matchedWords = new ArrayList<>();
        for (String word : filteredWords) {
            if (normalizedMessage.contains(word.toLowerCase())) {
                matchedWords.add(word);
            }
        }

        String[] rawWords = message
                .replace("(dot)", ".")
                .replace("(.)", ".")
                .replace("/./", ".")
                .replace("[dot]", ".").trim().split("\\s+");

        for (String word : rawWords) {
            if (isUrlOrIp(word)) {
                matchedWords.add(word);
            }
        }

        return matchedWords;
    }

    /**
     * Notify staff of a filtered message.
     *
     * @param player the player who sent the message
     * @param filteredWords the filtered words
     */
    private void notifyStaff(Player player, List<String> filteredWords) {
        ConfigService configService = plugin.getServiceManager().getService(ConfigService.class);
        String filteredWordsStr = String.join(", ", filteredWords);
        plugin.getServer().getOnlinePlayers().stream()
                .filter(staff -> staff.hasPermission("delta.staff"))
                .forEach(staff -> staff.sendMessage(CC.translate(configService.getConfig("messages.yml").getString("chat.filter.notify-staff")
                        .replace("%player%", player.getName())
                        .replace("%word%", filteredWordsStr))));
    }
}
