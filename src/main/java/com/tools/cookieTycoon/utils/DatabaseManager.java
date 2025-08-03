package com.tools.cookieTycoon.utils;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.tools.cookieTycoon.CookieTycoon;
import org.bson.Document;
import org.bukkit.entity.Player;

public class DatabaseManager {
    private CookieTycoon plugin;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> playersCollection;

    public DatabaseManager(CookieTycoon plugin, String connectionString, String databaseName) {
        this.plugin = plugin;
        try {
            this.mongoClient = MongoClients.create(connectionString);
            this.database = mongoClient.getDatabase(databaseName);
            this.playersCollection = database.getCollection("players");
            plugin.getLogger().info("Успешное подключение к MongoDB!");
        } catch (MongoException e) {
            plugin.getLogger().severe("ОШИБКА: Не удалось подключиться к MongoDB");
            plugin.getLogger().severe("Причина: " + e.getMessage());
        }
    }

    public Double getAllCookies(String playerUUID) {
        Document playerDoc = playersCollection.find(Filters.eq("uuid", playerUUID)).first();
        if (playerDoc != null && playerDoc.containsKey("allcookies")) {
            return playerDoc.getDouble("allcookies");
        }
        return (double) 0;
    }

    public void setAllCookies(String playerUUID, double cookies) {
        UpdateOptions options = new UpdateOptions().upsert(true);
        playersCollection.updateOne(
                Filters.eq("uuid", playerUUID),
                Updates.combine(
                        Updates.set("uuid", playerUUID),
                        Updates.set("allcookies", cookies)
                ),
                options
        );
    }

    public Double getCookies(String playerUUID) {
        Document playerDoc = playersCollection.find(Filters.eq("uuid", playerUUID)).first();
        if (playerDoc != null && playerDoc.containsKey("cookies")) {
            return playerDoc.getDouble("cookies");
        }
        return (double) 0;
    }

    public void setCookies(String playerUUID, double cookies) {
        UpdateOptions options = new UpdateOptions().upsert(true);
        playersCollection.updateOne(
                Filters.eq("uuid", playerUUID),
                Updates.combine(
                        Updates.set("uuid", playerUUID),
                        Updates.set("cookies", cookies)
                ),
                options
        );
    }

    public int getUpgrade(Player player, String upgradeName) {
        String playerUUID = String.valueOf(player.getUniqueId());
        Document playerDoc = playersCollection.find(Filters.eq("uuid", playerUUID)).first();
        if (playerDoc != null && playerDoc.containsKey("upgrades")) {
            Document upgrades = (Document) playerDoc.get("upgrades");
            return upgrades.getInteger(upgradeName, 0);
        }
        return 0;
    }

    public void setUpgrade(Player player, String upgradeName, int level) {
        String playerUUID = String.valueOf(player.getUniqueId());
        UpdateOptions options = new UpdateOptions().upsert(true);
        playersCollection.updateOne(
                Filters.eq("uuid", playerUUID),
                Updates.combine(
                        Updates.set("uuid", playerUUID),
                        Updates.set("upgrades." + upgradeName, level)
                ),
                options
        );
    }

    public int getRebirth(Player player) {
        String playerUUID = String.valueOf(player.getUniqueId());
        Document playerDoc = playersCollection.find(Filters.eq("uuid", playerUUID)).first();
        if (playerDoc != null && playerDoc.containsKey("rebirth")) {
            return playerDoc.getInteger("rebirth");
        }
        return 0;
    }

    public void setRebirth(Player player, int level) {
        String playerUUID = String.valueOf(player.getUniqueId());
        UpdateOptions options = new UpdateOptions().upsert(true);
        playersCollection.updateOne(
                Filters.eq("uuid", playerUUID),
                Updates.combine(
                        Updates.set("uuid", playerUUID),
                        Updates.set("rebirth", level)
                ),
                options
        );
    }

//    public int getCursor(String playerUUID) {
//        return getUpgrades(playerUUID, "cursor");
//    }
//
//    public void setCursor(String playerUUID, int level) {
//        setUpgrades(playerUUID, "cursor", level);
//    }
//
//    public int getGranny(String playerUUID) {
//        return getUpgrades(playerUUID, "granny");
//    }
//
//    public void setGranny(String playerUUID, int level) {
//        setUpgrades(playerUUID, "granny", level);
//    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}