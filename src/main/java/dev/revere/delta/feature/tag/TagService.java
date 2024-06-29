package dev.revere.delta.feature.tag;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.database.MongoService;
import dev.revere.delta.profile.Profile;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Delta
 * @date 6/29/2024
 */
@Getter
public class TagService implements IService {

    private final List<Tag> tags = new ArrayList<>();
    public MongoCollection<Document> collection;

    private final Delta plugin;

    /**
     * Constructor for the TagService class
     *
     * @param plugin the main class of the plugin
     */
    public TagService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        this.collection = getCollection();
        loadTagsFromDatabase();
    }

    /**
     * Add a tag to the list and save it to the database
     *
     * @param tag the tag to add
     */
    public void createTag(Tag tag) {
        saveTagToDatabase(tag);
        tags.add(tag);
    }

    /**
     * Delete a tag
     *
     * @param tag the tag to delete
     */
    public void deleteTag(Tag tag) {
        collection.deleteOne(Filters.eq("name", tag.getName()));
        tags.remove(tag);
    }

    /**
     * Get a tag by name
     *
     * @param name the name of the tag
     * @return the tag
     */
    public Tag getTag(String name) {
        return tags.stream().filter(tag -> tag.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Check if a tag exists
     *
     * @param name the name of the tag
     * @return if the tag exists
     */
    public boolean tagExists(String name) {
        return tags.stream().anyMatch(tag -> tag.getName().equalsIgnoreCase(name));
    }

    /**
     * Check if a player owns a tag
     *
     * @param player the player to check
     * @param tag the tag to check
     * @return if the player has the tag
     */
    public boolean hasTag(Player player, Tag tag) {
        return player.hasPermission("delta.tags." + tag.getName().toLowerCase());
    }

    /**
     * Get the permission of a tag
     *
     * @param tag the tag to get the permission of
     * @return the permission
     */
    public String getPermission(Tag tag) {
        return "delta.tags." + tag.getName().toLowerCase();
    }

    /**
     * Check if a player already has a tag selected
     *
     * @param profile the profile to check
     * @param tag the tag to check
     * @return if the player has the tag selected
     */
    public boolean hasSelected(Profile profile, Tag tag) {
        if (profile.getTag() == null) {
            return false;
        }
        return profile.getTag() != null && profile.getTag().getName().equalsIgnoreCase(tag.getName());
    }

    /**
     * Save a tag
     *
     * @param tag the tag to save
     */
    public void saveTag(Tag tag) {
        Document document = createDocumentFromTag(tag);
        collection.replaceOne(Filters.eq("name", tag.getName()), document);
    }

    /**
     * Save a tag to the database
     *
     * @param tag the rank to save
     */
    private void saveTagToDatabase(Tag tag) {
        Document document = createDocumentFromTag(tag);
        collection.insertOne(document);
    }

    /**
     * Create a document from a tag
     *
     * @param tag the tag to create the document from
     * @return the document
     */
    private Document createDocumentFromTag(Tag tag) {
        Document document = new Document();
        document.put("name", tag.getName());
        document.put("prefix", tag.getPrefix());
        document.put("color", tag.getColor().name());
        document.put("weight", tag.getWeight());
        document.put("purchasable", tag.isPurchasable());
        document.put("cost", tag.getCost());
        return document;
    }

    /**
     * Load a rag
     *
     * @param document the document to load the tag from
     * @return the tag
     */
    private Tag loadTag(Document document) {
        Tag tag = new Tag(document.getString("name"));
        tag.setPrefix(document.getString("prefix"));
        tag.setColor(ChatColor.valueOf(document.getString("color")));
        tag.setWeight(document.getInteger("weight"));
        tag.setPurchasable(document.getBoolean("purchasable"));
        tag.setCost(document.getInteger("cost"));
        return tag;
    }

    /**
     * Load tags from the database
     */
    private void loadTagsFromDatabase() {
        collection.find().forEach(document -> tags.add(loadTag(document)));
    }

    /**
     * Get the collection
     *
     * @return the collection
     */
    private MongoCollection<Document> getCollection() {
        return Delta.getInstance()
                .getServiceManager()
                .getService(MongoService.class)
                .getDatabase()
                .getCollection("tags");
    }
}
