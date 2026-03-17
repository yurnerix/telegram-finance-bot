package by.yurnerix.financebot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class Income {
    private int id;
    private int userId;
    private BigDecimal amount;
    private String source;
    private LocalDateTime createdAt;

    public Income() {
    }

    public Income(int id, int userId, BigDecimal amount, String source, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.source = source;
        this.createdAt = createdAt;
    }

    public Income(int userId, BigDecimal amount, String source) {
        this.userId = userId;
        this.amount = amount;
        this.source = source;
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

    public String getSource() {
        return source;
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

    public void setSource(String source) {
        this.source = source;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}