package dev.revere.delta.feature.server;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.database.MongoService;
import dev.revere.delta.service.ConfigService;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Location;

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

    private final List<Server> servers = new ArrayList<>();

    public MongoCollection<Document> collection;

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
        loadServersFromDatabase();
    }

    /**
     * Load a server
     *
     * @param document the document to load the server from
     * @return the server
     */
    private Server loadServer(Document document) {
        Server rank = new Server(document.getString("name"));
        rank.setSpawnLocation(documentToLocation(document.get("spawnpoint", Document.class)));
        rank.setWhitelistEnabled(document.getBoolean("whitelist", false));
        return rank;
    }

    /**
     * Load servers from the database
     */
    private void loadServersFromDatabase() {
        collection.find().forEach(document -> servers.add(loadServer(document)));
    }

    /**
     * Ensure that the server document exists in the database.
     * This method checks if the server document exists and creates it if it doesn't.
     */
    private void ensureServerDocumentExists() {
        String serverName = getServerName();
        Document existingDocument = collection.find(new Document("name", serverName)).first();

        if (existingDocument == null) {
            Document newDocument = new Document("name", serverName);
            newDocument.put("whitelist", false);
            newDocument.put("spawnpoint", null);

            collection.insertOne(newDocument);
        }
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
            serverDocument = new Document("name", serverName);
        }

        data.forEach(serverDocument::append);

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
     * Get a server by its name.
     *
     * @param serverName the name of the server
     * @return the server or null if not found
     */
    public Server getServer(String serverName) {
        return servers.stream().filter(server -> server.getName().equalsIgnoreCase(serverName)).findFirst().orElse(null);
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
     * Save the spawnpoint of a server.
     *
     * @param serverName the name of the server
     * @param location   the location of the spawnpoint
     */
    public void saveSpawnpoint(String serverName, Location location) {
        Document serverDocument = loadServerData(serverName);

        if (serverDocument == null) {
            serverDocument = new Document("name", serverName);
        }

        serverDocument.put("spawnpoint", locationToDocument(location));
        saveServerData(serverName, serverDocument);

        Server server = getServer(serverName);
        if (server != null) {
            server.setSpawnLocation(location);
        }
    }

    /**
     * Convert a location object to a MongoDB document.
     *
     * @param location the location to convert
     * @return the location as a document
     */
    private Document locationToDocument(Location location) {
        return new Document()
                .append("world", location.getWorld().getName())
                .append("x", location.getX())
                .append("y", location.getY())
                .append("z", location.getZ())
                .append("yaw", location.getYaw())
                .append("pitch", location.getPitch());
    }

    /**
     * Get the spawnpoint of a server.
     *
     * @param serverName the name of the server
     * @return the spawnpoint location
     */
    public Location getSpawnpoint(String serverName) {
        Server server = getServer(serverName);
        return (server != null) ? server.getSpawnLocation() : null;
    }

    /**
     * Convert a MongoDB document to a location object.
     *
     * @param document the document to convert
     * @return the location
     */
    private Location documentToLocation(Document document) {
        if (document == null) {
            return null;
        }

        String world = document.getString("world");
        double x = document.getDouble("x");
        double y = document.getDouble("y");
        double z = document.getDouble("z");
        float yaw = getFloatValue(document, "yaw");
        float pitch = getFloatValue(document, "pitch");

        return new Location(Delta.getInstance().getServer().getWorld(world), x, y, z, yaw, pitch);
    }

    /**
     * Get the float value from a document.
     *
     * @param document the document to get the value from
     * @param key      the key of the value
     * @return the float value
     */
    private float getFloatValue(Document document, String key) {
        Object value = document.get(key);
        if (value instanceof Double) {
            return ((Double) value).floatValue();
        } else if (value instanceof Float) {
            return (Float) value;
        }
        return 0;
    }

    /**
     * Set the whitelist status of a server.
     *
     * @param serverName the name of the server
     * @param enabled    the status of the whitelist
     */
    public void setWhitelistEnabled(String serverName, boolean enabled) {
        Document serverDocument = loadServerData(serverName);

        if (serverDocument == null) {
            serverDocument = new Document("name", serverName);
        }

        serverDocument.put("whitelist", enabled);
        saveServerData(serverName, serverDocument);

        Server server = getServer(serverName);
        if (server != null) {
            server.setWhitelistEnabled(enabled);
        }
    }

    /**
     * Check if whitelist is enabled for a server.
     *
     * @param serverName the name of the server
     * @return true if whitelist is enabled, false otherwise
     */
    public boolean isWhitelistEnabled(String serverName) {
        Server server = getServer(serverName);
        return (server != null) && server.isWhitelistEnabled();
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
