package dev.revere.revsentials.feature.clan;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/21/2024
 */
@Setter
@Getter
public class Clan {

    private final String name;
    private final UUID leader;
    private final Set<UUID> members;
    private final Set<UUID> invites;

    private ChatColor color = ChatColor.AQUA;
    private Location home;

    /**
     * Create a new clan
     *
     * @param name   the name of the clan
     * @param leader the leader of the clan
     */
    public Clan(String name, UUID leader) {
        this.name = name;
        this.leader = leader;
        this.invites = new HashSet<>();
        this.members = new HashSet<>();
        this.members.add(leader);
    }

    /**
     * Add a member to the clan
     *
     * @param member the member to add
     */
    public void addMember(UUID member) {
        members.add(member);
        invites.remove(member);
    }

    /**
     * Remove a member from the clan
     *
     * @param member the member to remove
     */
    public void removeMember(UUID member) {
        members.remove(member);
    }

    /**
     * Check if a player is a member of the clan
     *
     * @param member the member to check
     * @return true if the player is a member of the clan
     */
    public boolean isMember(UUID member) {
        return members.contains(member);
    }

    /**
     * Check if a player is the leader of the clan
     *
     * @param member the member to check
     * @return true if the player is the leader of the clan
     */
    public boolean isLeader(UUID member) {
        return leader.equals(member);
    }

    /**
     * Add an invite to the clan
     *
     * @param invite the invite to add
     */
    public void addInvite(UUID invite) {
        invites.add(invite);
    }

    /**
     * Remove an invite from the clan
     *
     * @param invite the invite to remove
     */
    public void removeInvite(UUID invite) {
        invites.remove(invite);
    }

    /**
     * Check if a player is invited to the clan
     *
     * @param invite the invite to check
     * @return true if the player is invited to the clan
     */
    public boolean isInvited(UUID invite) {
        return invites.contains(invite);
    }

    /**
     * Get the colored name of the clan
     *
     * @return the colored name of the clan
     */
    public String getColoredName() {
        return color + name;
    }
}