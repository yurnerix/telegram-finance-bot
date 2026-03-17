package by.yurnerix.financebot.util;

import by.yurnerix.financebot.model.FinanceSummary;

import java.math.BigDecimal;


public class FinanceSummaryBuilder {

    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal totalExpense = BigDecimal.ZERO;
    private BigDecimal balance = BigDecimal.ZERO;
    private String goalName = "Не установлена";
    private BigDecimal targetAmount = BigDecimal.ZERO;
    private BigDecimal remainingToSave = BigDecimal.ZERO;

    public FinanceSummaryBuilder totalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
        return this;
    }

    public FinanceSummaryBuilder totalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
        return this;
    }

    public FinanceSummaryBuilder balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public FinanceSummaryBuilder goalName(String goalName) {
        this.goalName = goalName;
        return this;
    }

    public FinanceSummaryBuilder targetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
        return this;
    }

    public FinanceSummaryBuilder remainingToSave(BigDecimal remainingToSave) {
        this.remainingToSave = remainingToSave;
        return this;
    }

    public FinanceSummary build() {
        return new FinanceSummary(
                totalIncome,
                totalExpense,
                balance,
                goalName,
                targetAmount,
                remainingToSave
        );
    }
}