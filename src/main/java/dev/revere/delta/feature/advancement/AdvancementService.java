package dev.revere.delta.feature.advancement;

import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.profile.Profile;
import dev.revere.delta.profile.ProfileService;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Remi
 * @project Delta
 * @date 6/30/2024
 */
@Getter
public class AdvancementService implements IService {

    private Map<UUID, Map<AdvancementCategory, List<Advancement>>> playerIncompleteAdvancements;
    private Map<UUID, Map<AdvancementCategory, List<Advancement>>> playerCompletedAdvancements;
    private Map<AdvancementCategory, List<Advancement>> categorizedAdvancements;

    private final Delta plugin;

    /**
     * Constructor for the AdvancementService.
     *
     * @param plugin the plugin instance
     */
    public AdvancementService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        Bukkit.getPluginManager().registerEvents(new AdvancementListener(), plugin);
        this.playerIncompleteAdvancements = new HashMap<>();
        this.playerCompletedAdvancements = new HashMap<>();
        this.categorizedAdvancements = new HashMap<>();
        loadAdvancements();
    }

    /**
     * Load all advancements for a player.
     *
     * @param player the player to load advancements for
     */
    public void loadPlayerAdvancements(Player player) {
        Map<AdvancementCategory, List<Advancement>> completedAdvancements = new HashMap<>();
        Map<AdvancementCategory, List<Advancement>> incompleteAdvancements = new HashMap<>();

        for (AdvancementCategory category : AdvancementCategory.values()) {
            List<Advancement> advancements = categorizedAdvancements.getOrDefault(category, Collections.emptyList());
            List<Advancement> completed = new ArrayList<>();
            List<Advancement> incomplete = new ArrayList<>();

            for (Advancement advancement : advancements) {
                String advancementKey = advancement.getKey().getKey();
                if (hasAdvancement(player, advancementKey)) {
                    completed.add(advancement);
                } else {
                    incomplete.add(advancement);
                }
            }

            completedAdvancements.put(category, completed);
            incompleteAdvancements.put(category, incomplete);
        }

        playerCompletedAdvancements.put(player.getUniqueId(), completedAdvancements);
        playerIncompleteAdvancements.put(player.getUniqueId(), incompleteAdvancements);
    }

    /**
     * Load all advancements on the server.
     */
    private void loadAdvancements() {
        List<Advancement> advancements = getAdvancements();
        for (Advancement advancement : advancements) {
            String advancementKey = advancement.getKey().getKey();

            AdvancementCategory category = getAdvancementCategory(advancementKey);

            if (category != null) {
                categorizedAdvancements.computeIfAbsent(category, k -> new ArrayList<>()).add(advancement);
            }
        }
    }

    /**
     * Get the category of an advancement.
     *
     * @param advancementKey the key of the advancement
     * @return the category of the advancement
     */
    public AdvancementCategory getAdvancementCategory(String advancementKey) {
        for (AdvancementCategory category : AdvancementCategory.values()) {
            String categoryNamespaceWithoutMinecraft = category.getNamespace().replace("minecraft:", "");
            if (advancementKey.startsWith(categoryNamespaceWithoutMinecraft + "/")) {
                return category;
            }
        }
        return null;
    }

    /**
     * Get a list of completed advancements for a player.
     *
     * @param player the player to get advancements for
     * @return a list of completed advancements
     */
    public List<Advancement> getCompletedAdvancements(Player player, AdvancementCategory category) {
        Map<AdvancementCategory, List<Advancement>> playerAdvancements = playerCompletedAdvancements.get(player.getUniqueId());
        if (playerAdvancements == null) {
            return Collections.emptyList();
        }
        return playerAdvancements.getOrDefault(category, Collections.emptyList());
    }

    /**
     * Get a list of incomplete advancements for a player.
     *
     * @param player the player to get advancements for
     * @return a list of incomplete advancements
     */
    public List<Advancement> getIncompleteAdvancements(Player player, AdvancementCategory category) {
        Map<AdvancementCategory, List<Advancement>> playerAdvancements = playerIncompleteAdvancements.get(player.getUniqueId());
        if (playerAdvancements == null) {
            return Collections.emptyList();
        }
        return playerAdvancements.getOrDefault(category, Collections.emptyList());
    }

    /**
     * Get a list of incomplete advancements for a player.
     *
     * @param player the player to get advancements for
     * @return a list of incomplete advancements
     */
    public List<Advancement> getIncompleteAdvancements(Player player) {
        Map<AdvancementCategory, List<Advancement>> playerAdvancements = playerIncompleteAdvancements.get(player.getUniqueId());
        if (playerAdvancements == null) {
            return Collections.emptyList();
        }
        List<Advancement> advancements = new ArrayList<>();
        for (List<Advancement> list : playerAdvancements.values()) {
            advancements.addAll(list);
        }
        return advancements;
    }

    /**
     * Get a list of completed advancements for a player.
     *
     * @param player the player to get advancements for
     * @return a list of completed advancements
     */
    public List<Advancement> getCompletedAdvancements(Player player) {
        Map<AdvancementCategory, List<Advancement>> playerAdvancements = playerCompletedAdvancements.get(player.getUniqueId());
        if (playerAdvancements == null) {
            return Collections.emptyList();
        }
        List<Advancement> advancements = new ArrayList<>();
        for (List<Advancement> list : playerAdvancements.values()) {
            advancements.addAll(list);
        }
        return advancements;
    }

    /**
     * Marks an advancement as redeemed for a player.
     *
     * @param player the player to mark the advancement as redeemed for
     * @param name   the name of the advancement
     */
    public void markAdvancementAsRedeemed(Player player, String name) {
        Profile profile = plugin.getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile != null) {
            profile.getRedeemedAdvancements().add(name);
        }
    }

    /**
     * Marks an advancement as completed for a player.
     *
     * @param player         the player to mark the advancement as completed for
     * @param advancementKey the key of the advancement
     */
    public void markAdvancementAsCompleted(Player player, String advancementKey) {
        Advancement advancement = getAdvancement(advancementKey);
        if (advancement != null) {
            Map<AdvancementCategory, List<Advancement>> completedAdvancements = playerCompletedAdvancements.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
            Map<AdvancementCategory, List<Advancement>> incompleteAdvancements = playerIncompleteAdvancements.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());

            AdvancementCategory category = getAdvancementCategory(advancementKey);
            if (category != null) {
                List<Advancement> completed = completedAdvancements.computeIfAbsent(category, k -> new ArrayList<>());
                List<Advancement> incomplete = incompleteAdvancements.computeIfAbsent(category, k -> new ArrayList<>());

                incomplete.removeIf(adv -> adv.getKey().getKey().equals(advancementKey));
                completed.add(advancement);

                loadPlayerAdvancements(player);
            }
        }
    }

    /**
     * Checks if a player has redeemed a specific advancement.
     *
     * @param player the player to check
     * @param name   the name of the advancement
     * @return true if the player has redeemed the advancement.
     */
    public boolean hasAdvancementRedeemed(Player player, String name) {
        Profile profile = plugin.getServiceManager().getService(ProfileService.class).getProfile(player.getUniqueId());
        return profile != null && profile.getRedeemedAdvancements().contains(name);
    }

    /**
     * Checks if a player has a specific advancement.
     *
     * @param player the player to check
     * @param name   the name of the advancement
     * @return true if the player has the advancement.
     */
    public boolean hasAdvancement(Player player, String name) {
        Advancement advancement = getAdvancement(name);
        if(advancement == null) {
            return false;
        }
        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        return progress.isDone();
    }

    /**
     * Get an advancement by its name.
     *
     * @param name the name of the advancement
     * @return the advancement
     */
    public Advancement getAdvancement(String name) {
        Advancement advancement = Bukkit.getServer().getAdvancement(new NamespacedKey("minecraft", name));
        if (advancement == null) {
            Bukkit.getLogger().warning("Advancement with key '" + name + "' does not exist.");
        }
        return advancement;
    }

    /**
     * Get a list of advancements for a specific category.
     *
     * @param category the category to get advancements for
     * @return a list of advancements
     */
    public List<Advancement> getAdvancements(AdvancementCategory category) {
        return categorizedAdvancements.getOrDefault(category, Collections.emptyList());
    }

    /**
     * @return a list of all advancements on the server.
     */
    public List<Advancement> getAdvancements() {
        List<Advancement> advancements = new ArrayList<>();
        Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
        while (iterator.hasNext()) {
            advancements.add(iterator.next());
        }
        return advancements;
    }
}
