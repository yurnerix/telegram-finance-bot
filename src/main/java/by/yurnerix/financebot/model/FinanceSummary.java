package by.yurnerix.financebot.model;

import java.math.BigDecimal;

public class FinanceSummary {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private String goalName;
    private BigDecimal targetAmount;
    private BigDecimal remainingToSave;

    public FinanceSummary() {
    }

    public FinanceSummary(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal balance,
                          String goalName, BigDecimal targetAmount, BigDecimal remainingToSave) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = balance;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.remainingToSave = remainingToSave;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getGoalName() {
        return goalName;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public BigDecimal getRemainingToSave() {
        return remainingToSave;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public void setRemainingToSave(BigDecimal remainingToSave) {
        this.remainingToSave = remainingToSave;
    }
}