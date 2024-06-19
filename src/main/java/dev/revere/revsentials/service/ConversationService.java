package dev.revere.revsentials.service;

import dev.revere.revsentials.Revsential;
import dev.revere.revsentials.api.service.Service;
import dev.revere.revsentials.cooldown.Cooldown;
import dev.revere.revsentials.util.CC;
import dev.revere.revsentials.util.triple.MutableTriple;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Remi
 * @project Revsentials
 * @date 6/19/2024
 */
@Getter
public class ConversationService implements Service {

    private final HashMap<UUID, UUID> conversations;

    private final Revsential plugin;

    /**
     * Constructor for the ConversationService class
     *
     * @param plugin the main class of the plugin
     */
    public ConversationService(Revsential plugin) {
        this.plugin = plugin;
        this.conversations = new HashMap<>();
    }

    @Override
    public void register() {

    }

    /**
     * Send a message to a player in a conversation
     *
     * @param sender   the UUID of the sender
     * @param receiver the UUID of the receiver
     * @param message  the message to send
     */
    public void sendMessages(UUID sender, UUID receiver, String message) {
        startConversation(sender, receiver);

        FileConfiguration config = plugin.getServiceManager().getService(ConfigService.class).getConfigByName("messages.yml");
        Player player = Bukkit.getPlayer(sender);
        Player target = Bukkit.getPlayer(receiver);

        if (player == null) {
            return;
        }

        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        target.sendMessage(CC.translate(Objects.requireNonNull(config.getString("conversation.from"))
                .replace("%sender%", player.getName())
                .replace("%message%", message)));
        target.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

        player.sendMessage(CC.translate(Objects.requireNonNull(config.getString("conversation.to"))
                .replace("%receiver%", target.getName())
                .replace("%message%", message)));
    }

    /**
     * Start a conversation between two players
     *
     * @param sender   the UUID of the sender
     * @param receiver the UUID of the receiver
     */
    public void startConversation(UUID sender, UUID receiver) {
        conversations.put(sender, receiver);
        conversations.put(receiver, sender);
    }

    /**
     * Get the conversation of a player
     *
     * @param uuid the UUID of the player
     * @return the conversation
     */
    public UUID getConversation(UUID uuid) {
        return conversations.get(uuid);
    }
}
