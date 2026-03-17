package by.yurnerix.financebot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class SavingGoal {
    private int id;
    private int userId;
    private BigDecimal targetAmount;
    private String goalName;
    private LocalDateTime createdAt;

    public SavingGoal() {
    }

    public SavingGoal(int id, int userId, BigDecimal targetAmount, String goalName, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.targetAmount = targetAmount;
        this.goalName = goalName;
        this.createdAt = createdAt;
    }

    public SavingGoal(int userId, BigDecimal targetAmount, String goalName) {
        this.userId = userId;
        this.targetAmount = targetAmount;
        this.goalName = goalName;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public String getGoalName() {
        return goalName;
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

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
