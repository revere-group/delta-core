package dev.revere.delta.feature.cooldown;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.util.triple.MutableTriple;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
@Getter
public class CooldownService implements IService {

    private final Delta plugin;

    private final List<MutableTriple<UUID, String, Cooldown>> cooldowns;

    /**
     * Constructor for the CooldownService class
     *
     * @param plugin the main class of the plugin
     */
    public CooldownService(Delta plugin) {
        this.plugin = plugin;
        this.cooldowns = new ArrayList<>();
    }

    @Override
    public void register() {
    }

    /**
     * Add a cooldown to the list
     *
     * @param uuid     the UUID of the cooldown
     * @param key      the key of the cooldown
     * @param cooldown the cooldown to add
     */
    public void addCooldown(UUID uuid, String key, Cooldown cooldown) {
        cooldowns.removeIf(triple -> triple.getA().equals(uuid) && triple.getB().equals(key));
        cooldowns.add(new MutableTriple<>(uuid, key, cooldown));
    }

    /**
     * Get a cooldown by its UUID and key
     *
     * @param uuid the UUID of the cooldown
     * @param key  the key of the cooldown
     * @return the cooldown
     */
    public Cooldown getCooldown(UUID uuid, String key) {
        return cooldowns.stream()
                .filter(triple -> triple.getA().equals(uuid) && triple.getB().equals(key))
                .map(MutableTriple::getC)
                .findFirst()
                .orElse(null);
    }
}
