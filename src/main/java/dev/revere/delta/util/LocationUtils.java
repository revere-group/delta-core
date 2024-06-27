package dev.revere.delta.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Emmy
 * @project Delta
 * @date 27/06/2024 - 19:48
 */
@UtilityClass
public class LocationUtils {

    /**
     * Teleports a player up to the highest block at their current location.
     *
     * @param loc The location to teleport up from.
     * @return The new location.
     */
    public Location teleportUp(Location loc) {
        return new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    /**
     * Teleports a player down to the lowest block at their current location.
     *
     * @param start The location to teleport down from.
     * @return The new location.
     */
    public Location[] getFaces(Location start) {
        Location[] faces = new Location[4];
        faces[0] = new Location(start.getWorld(), start.getX() + 1, start.getY(), start.getZ());
        faces[1] = new Location(start.getWorld(), start.getX() - 1, start.getY(), start.getZ());
        faces[2] = new Location(start.getWorld(), start.getX(), start.getY() + 1, start.getZ());
        faces[3] = new Location(start.getWorld(), start.getX(), start.getY() - 1, start.getZ());
        return faces;
    }

    /**
     * Serializes a location to a string.
     *
     * @param location The location to serialize.
     * @return The serialized location.
     */
    public String serialize(Location location) {
        if (location == null) {
            return "null";
        }

        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() +
                ":" + location.getYaw() + ":" + location.getPitch();
    }

    /**
     * Deserializes a location from a string.
     *
     * @param source The string to deserialize.
     * @return The deserialized location.
     */
    public Location deserialize(String source) {
        if (source == null || source.equalsIgnoreCase("null")) {
            return null;
        }

        String[] split = source.split(":");
        World world = Bukkit.getServer().getWorld(split[0]);

        if (world == null) {
            return null;
        }

        return new Location(world, Double.parseDouble(split[1]), Double.parseDouble(split[2]),
                Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }
}
