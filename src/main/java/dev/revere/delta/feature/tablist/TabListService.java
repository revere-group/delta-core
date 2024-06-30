package dev.revere.delta.feature.tablist;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.feature.rank.Rank;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project Delta
 * @date 6/30/2024
 */
@Getter
public class TabListService implements IService {

    private final Delta plugin;

    /**
     * Constructor for the TabListService class
     *
     * @param delta the main class of the plugin
     */
    public TabListService(Delta delta) {
        this.plugin = delta;
    }

    @Override
    public void register() {
        ConfigService configService = plugin.getServiceManager().getService(ConfigService.class);
        if (configService.getConfig("settings.yml").getBoolean("tablist.enabled")) {
            startUpdatingTabList();
        }
    }

    /**
     * Set the player list header and footer for a player
     *
     * @param player the player to set the header and footer for
     * @param header the header
     * @param footer the footer
     */
    public void setPlayerListHeaderFooter(Player player, String header, String footer) {
        player.setPlayerListHeaderFooter(CC.translate(header), CC.translate(footer));
    }

    /**
     * Set the player list name for a player
     *
     * @param player the player to set the name for
     * @param name   the name
     */
    public void setPlayerListName(Player player, String name) {
        player.setPlayerListName(CC.translate(name));
    }

    /**
     * Update the player list names of all online players
     */
    public void updatePlayerListNames() {
        RankService rankService = plugin.getServiceManager().getService(RankService.class);
        ConfigService configService = plugin.getServiceManager().getService(ConfigService.class);

        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = plugin.getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());
            Rank rank = rankService.getHighestRank(profile);

            boolean smallFont = configService.getConfig("settings.yml").getBoolean("tablist.small-font");

            String playerName = smallFont ? CC.toSmallFont(player.getName()) : player.getName();
            String rankPrefix = smallFont ? CC.toSmallFont(CC.translate(rank.getPrefix())) : rank.getPrefix();

            String header = smallFont ? CC.toSmallFont(CC.translate(configService.getConfig("settings.yml").getString("tablist.header"))) : configService.getConfig("settings.yml").getString("tablist.header");
            String footer = smallFont ? CC.toSmallFont(CC.translate(configService.getConfig("settings.yml").getString("tablist.footer"))) : configService.getConfig("settings.yml").getString("tablist.footer");

            setPlayerListName(player, rankPrefix + " " + rank.getNameColor() + playerName);
            setPlayerListHeaderFooter(player, header, footer);
        }
    }

    /**
     * Start a task that updates the player list names every second.
     */
    private void startUpdatingTabList() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updatePlayerListNames();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
