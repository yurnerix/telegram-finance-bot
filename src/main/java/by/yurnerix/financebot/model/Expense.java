package by.yurnerix.financebot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Expense {
    private int id;
    private int userId;
    private BigDecimal amount;
    private String category;
    private String description;
    private LocalDateTime createdAt;

    public Expense() {
    }

    public Expense(int id, int userId, BigDecimal amount, String category,
                   String description, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Expense(int userId, BigDecimal amount, String category, String description) {
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}