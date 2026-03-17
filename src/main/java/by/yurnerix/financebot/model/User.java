package by.yurnerix.financebot.model;

import java.time.LocalDateTime;

public class User {
    private int id;
    private long telegramId;
    private String name;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(int id, long telegramId, String name, LocalDateTime createdAt) {
        this.id = id;
        this.telegramId = telegramId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public User(long telegramId, String name) {
        this.telegramId = telegramId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public long getTelegramId() {
        return telegramId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTelegramId(long telegramId) {
        this.telegramId = telegramId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}