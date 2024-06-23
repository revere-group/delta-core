package dev.revere.delta.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dev.revere.delta.Delta;
import dev.revere.delta.api.service.IService;
import dev.revere.delta.service.ConfigService;
import dev.revere.delta.util.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Remi
 * @project Delta
 * @date 6/23/2024
 */
@Getter
public class MongoService implements IService {

    private final Delta plugin;

    private MongoDatabase database;
    private MongoClient client;

    /**
     * Constructor for the MongoService class
     *
     * @param plugin the main class of the plugin
     */
    public MongoService(Delta plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register() {
        try {
            FileConfiguration config = Delta.getInstance().getServiceManager().getService(ConfigService.class).getConfig("database/database.yml");

            String databaseName = config.getString("mongo.databaseName");
            Bukkit.getConsoleSender().sendMessage(CC.translate("&bConnecting to the MongoDB database..."));

            ConnectionString connectionString = new ConnectionString(Objects.requireNonNull(config.getString("mongo.connectionString")));
            MongoClientSettings.Builder settings = MongoClientSettings.builder();
            settings.applyConnectionString(connectionString);
            settings.applyToConnectionPoolSettings(builder -> builder.maxConnectionIdleTime(30, TimeUnit.SECONDS));
            settings.retryWrites(true);

            this.client = MongoClients.create(settings.build());
            this.database = client.getDatabase(databaseName);

            Bukkit.getConsoleSender().sendMessage(CC.translate("&bSuccessfully connected to the MongoDB database."));
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cFailed to connect to the MongoDB database."));
        }
    }
}
