package dev.revere.delta.feature.server;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.database.MongoService;
import dev.revere.delta.service.ConfigService;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/26/2024
 */
@Getter
public class ServerService implements IService {

    public MongoCollection<Document> collection;

    private final List<String> servers = new ArrayList<>();

    private final Delta plugin;

    /**
     * Constructor for the ServerService class
     *
     * @param plugin the main class of the plugin
     */
    public ServerService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        this.collection = getCollection();
        ensureServerDocumentExists();
    }

    /**
     * Ensure that the server document exists in the database.
     * This method checks if the server document exists and creates it if it doesn't.
     */
    private void ensureServerDocumentExists() {
        String serverName = getServerName();
        Document existingDocument = collection.find(new Document("name", serverName)).first();

        if (existingDocument == null) {
            Document newDocument = new Document("name", serverName).append("data", new Document());
            collection.insertOne(newDocument);
        }

        servers.add(serverName);
    }

    /**
     * Save server data to the database.
     *
     * @param serverName the name of the server
     * @param data       the data to save (can be any type of object)
     */
    public void saveServerData(String serverName, Map<String, Object> data) {
        Document serverDocument = loadServerData(serverName);

        if (serverDocument == null) {
            serverDocument = new Document("name", serverName).append("data", new Document());
        }

        Document dataDocument = (Document) serverDocument.get("data");
        dataDocument.putAll(data);

        serverDocument.put("data", dataDocument);

        collection.replaceOne(new Document("name", serverName), serverDocument, new ReplaceOptions().upsert(true));
    }

    /**
     * Load server data from the database.
     *
     * @param serverName the name of the server
     * @return the server document or null if not found
     */
    public Document loadServerData(String serverName) {
        return collection.find(new Document("name", serverName)).first();
    }

    /**
     * Get the server name from the database.
     *
     * @return the server name
     */
    public String getServer(String serverName) {
        return servers.stream().filter(server -> server.equalsIgnoreCase(serverName)).findFirst().orElse(null);
    }

    /**
     * Get specific server data value.
     *
     * @param serverName the name of the server
     * @param key        the key of the data to retrieve
     * @return the value associated with the key, or null if not found
     */
    public Object getServerData(String serverName, String key) {
        Document serverDocument = loadServerData(serverName);
        if (serverDocument != null && serverDocument.containsKey("data")) {
            Document data = (Document) serverDocument.get("data");
            return data.get(key);
        }
        return null;
    }

    /**
     * Get the name of the server.
     *
     * @return the name of the server
     */
    public String getServerName() {
        return Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("settings.yml").getString("server-name");
    }

    /**
     * Check if whitelist is enabled for a server.
     *
     * @param serverName the name of the server
     * @return true if whitelist is enabled, false otherwise
     */
    public boolean isWhitelistEnabled(String serverName) {
        Object whitelist = getServerData(serverName, "whitelist");
        return whitelist instanceof Boolean && (Boolean) whitelist;
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
                .getCollection("servers");
    }
}
