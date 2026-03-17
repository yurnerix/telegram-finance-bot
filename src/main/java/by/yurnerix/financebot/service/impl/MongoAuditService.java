package by.yurnerix.financebot.service.impl;

import by.yurnerix.financebot.config.MongoManager;
import by.yurnerix.financebot.service.AuditService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDateTime;


public class MongoAuditService implements AuditService {

    private final MongoCollection<Document> actionsCollection;
    private final MongoCollection<Document> snapshotsCollection;

    public MongoAuditService() {
        MongoDatabase database = MongoManager.getDatabase();
        this.actionsCollection = database.getCollection("user_actions");
        this.snapshotsCollection = database.getCollection("finance_snapshots");
    }

    @Override
    public void logAction(long telegramId, String action, String details) {
        Document document = new Document()
                .append("telegramId", telegramId)
                .append("action", action)
                .append("details", details)
                .append("createdAt", LocalDateTime.now().toString());

        actionsCollection.insertOne(document);
    }

    @Override
    public void saveSnapshot(long telegramId, String totalIncome, String totalExpense,
                             String balance, String goalName, String targetAmount) {
        Document document = new Document()
                .append("telegramId", telegramId)
                .append("totalIncome", totalIncome)
                .append("totalExpense", totalExpense)
                .append("balance", balance)
                .append("goalName", goalName)
                .append("targetAmount", targetAmount)
                .append("createdAt", LocalDateTime.now().toString());

        snapshotsCollection.insertOne(document);
    }
}