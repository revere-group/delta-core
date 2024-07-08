package dev.revere.delta.util;

import dev.revere.delta.Delta;
import dev.revere.delta.feature.rank.RankService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author Remi
 * @project Delta
 * @date 6/30/2024
 */
public class PlaceholderAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "delta";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", Delta.getInstance().getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return Delta.getInstance().getDescription().getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return null;
        }

        UUID uuid = player.getUniqueId();
        Delta deltaInstance = Delta.getInstance();
        ProfileService profileService = deltaInstance.getServiceManager().getService(ProfileService.class);
        RankService rankService = deltaInstance.getServiceManager().getService(RankService.class);
        Profile profile = profileService.getProfile(uuid);
        if (profile == null) {
            return null;
        }

        return switch (params) {
            case "rank-prefix" -> CC.translate(rankService.getHighestRank(profile).getPrefix());
            case "rank-prefix-font" -> CC.toSmallFont(CC.translate(rankService.getHighestRank(profile).getPrefix()));
            case "rank-suffix" -> CC.translate(rankService.getHighestRank(profile).getSuffix());
            case "rank-name" -> rankService.getHighestRank(profile).getName();
            case "rank-color" -> rankService.getHighestRank(profile).getNameColor().toString();
            case "rank-weight" -> String.valueOf(rankService.getHighestRank(profile).getWeight());
            case "rank-inheritance" -> rankService.getHighestRank(profile).getInheritance().toString();
            case "tag-name" -> CC.translate(profile.getTag() != null ? profile.getTag().getName() : "");
            case "tag-prefix" -> CC.translate(profile.getTag() != null ? " " + profile.getTag().getPrefix() : "");
            case "tag-color" -> profile.getTag() != null ? profile.getTag().getColor().toString() : "";
            default -> null;
        };
    }
}
