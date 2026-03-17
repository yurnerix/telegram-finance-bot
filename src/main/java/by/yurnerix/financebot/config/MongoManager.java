package by.yurnerix.financebot.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public final class MongoManager {

    private static final MongoClient MONGO_CLIENT =
            MongoClients.create(AppConfig.get("mongo.connection"));

    private MongoManager() {
    }

    public static MongoDatabase getDatabase() {
        return MONGO_CLIENT.getDatabase(AppConfig.get("mongo.database"));
    }
}