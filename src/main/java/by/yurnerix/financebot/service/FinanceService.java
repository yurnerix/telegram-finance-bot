package by.yurnerix.financebot.service;

public interface FinanceService {
    String registerUser(long telegramId, String name);
    String addIncome(long telegramId, String amountText, String source);
    String addExpense(long telegramId, String amountText, String category, String description);
    String getBalance(long telegramId);
    String setSavingGoal(long telegramId, String amountText, String goalName);
    String getSummary(long telegramId);

    String getHelp();
}