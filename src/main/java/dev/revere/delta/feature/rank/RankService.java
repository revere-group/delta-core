package dev.revere.delta.feature.rank;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.database.MongoService;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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
     * Get the default rank
     *
     * @return the default rank
     */
    public Rank getDefaultRank() {
        return ranks.stream()
                .filter(Rank::isDefaultRank)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a rank by its weight
     *
     * @param weight the weight of the rank
     * @return the rank
     */
    public Rank getRank(int weight) {
        return ranks.stream()
                .filter(rank -> rank.getWeight() == weight)
                .findFirst()
                .orElse(null);
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
        rank.setDefaultRank(document.getBoolean("defaultRank"));
        rank.setPermissions((List<String>) document.get("permissions"));

        return rank;
    }

    /**
     * Load a rank from the database
     *
     * @param name the name of the rank
     * @return the rank
     */
    public Rank loadRank(String name) {
        Document document = collection.find(Filters.eq("name", name)).first();
        return document != null ? loadRank(document) : null;
    }

    /**
     * Load all ranks from the database
     */
    public void loadRanksFromDatabase() {
        collection.find().forEach(document -> ranks.add(loadRank(document)));
    }

    /**
     * Save all ranks to the database
     */
    public void saveRanksToDatabase() {
        ranks.forEach(this::saveRank);
    }
}
