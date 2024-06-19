package dev.revere.revsentials.profile;

import dev.revere.revsentials.profile.staff.setting.StaffOptions;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
@Getter
@Setter
public class Profile {
    private String name;
    private UUID uuid;

    private boolean online;

    private final StaffOptions staffOptions;

    /**
     * Constructor for the Profile class
     *
     * @param uuid the UUID of the profile
     */
    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(this.uuid).getName();
        this.staffOptions = new StaffOptions();
    }
}
