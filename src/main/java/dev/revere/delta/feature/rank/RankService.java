package dev.revere.delta.feature.rank;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.database.MongoService;
import dev.revere.delta.feature.grant.Grant;
import dev.revere.delta.feature.grant.GrantService;
import dev.revere.delta.profile.Profile;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
@Getter
public class RankService implements IService {

    private final List<Rank> ranks = new ArrayList<>();
    public MongoCollection<Document> collection;

    private final Delta plugin;

    /**
     * Constructor for the RankService class
     *
     * @param plugin the main class of the plugin
     */
    public RankService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        this.collection = Delta.getInstance().getServiceManager().getService(MongoService.class).getDatabase().getCollection("ranks");
        this.collection.find().forEach(document -> ranks.add(loadRank(document)));

        Rank defaultRank = getDefaultRank();
        if (defaultRank == null) {
            createDefaultRank();
        }
    }

    /**
     * Add a rank to the list
     *
     * @param rank the rank to add
     */
    public void createRank(Rank rank) {
        Document document = new Document();
        document.put("name", rank.getName());

        collection.insertOne(document);
        ranks.add(rank);
    }

    /**
     * Delete a rank from the list
     *
     * @param rank the rank to remove
     */
    public void deleteRank(Rank rank) {
        collection.deleteOne(Filters.eq("name", rank.getName()));
        ranks.remove(rank);
    }

    /**
     * Get the highest rank that a player has
     *
     * @param profile the profile to get the rank from
     * @return the highest rank
     */
    public Rank getHighestRank(Profile profile) {
        GrantService grantService = Delta.getInstance().getServiceManager().getService(GrantService.class);
        return grantService.getActiveGrants(profile).stream().map(Grant::getRank).filter(Objects::nonNull).max(Comparator.comparingInt(Rank::getWeight)).orElse(getDefaultRank());
    }

    /**
     * Get a rank by its name
     *
     * @param name the name of the rank
     * @return the rank
     */
    public Rank getRank(String name) {
        return ranks.stream()
                .filter(rank -> rank.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if a rank exists
     *
     * @param name the name of the rank
     * @return if the rank exists
     */
    public boolean rankExists(String name) {
        return ranks.stream().anyMatch(rank -> rank.getName().equalsIgnoreCase(name));
    }

    /**
     * Save a rank
     *
     * @param rank the rank to save
     */
    public void saveRank(Rank rank) {
        Document document = new Document();
        document.put("name", rank.getName());
        document.put("prefix", rank.getPrefix());
        document.put("suffix", rank.getSuffix());
        document.put("weight", rank.getWeight());
        document.put("nameColor", rank.getNameColor().toString());
        document.put("defaultRank", rank.isDefaultRank());
        document.put("permissions", rank.getPermissions());

        collection.replaceOne(Filters.eq("name", rank.getName()), document);
    }

    /**
     * Load a rank
     *
     * @param document the document to load the rank from
     * @return the rank
     */
    private Rank loadRank(Document document) {
        Rank rank = new Rank(document.getString("name"));
        rank.setPrefix(document.getString("prefix"));
        rank.setSuffix(document.getString("suffix"));
        rank.setWeight(document.getInteger("weight"));
        rank.setNameColor(ChatColor.getByChar(document.getString("nameColor").charAt(1)));
        rank.setDefaultRank(document.getBoolean("defaultRank"));
        rank.setPermissions((List<String>) document.get("permissions"));
        return rank;
    }

    /**
     * Create the default rank
     */
    public void createDefaultRank() {
        Rank rank = new Rank("Default");
        rank.setPrefix("&aDefault");
        rank.setSuffix("");
        rank.setWeight(0);
        rank.setNameColor(ChatColor.GREEN);
        rank.setDefaultRank(true);
        rank.setPermissions(new ArrayList<>());
        createRank(rank);
        saveRank(rank);
    }

    /**
     * Get the default rank
     *
     * @return the default rank
     */
    public Rank getDefaultRank() {
        return this.ranks.stream().sorted(Comparator.comparingInt(Rank::getWeight).reversed()).filter(Rank::isDefaultRank).findFirst().orElse(null);
    }

    /**
     * Check if a rank is the default rank
     *
     * @param rank the rank to check
     * @return if the rank is the default rank
     */
    public boolean isDefaultRank(Rank rank) {
        return rank.isDefaultRank();
    }
}
