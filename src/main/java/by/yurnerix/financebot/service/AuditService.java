package by.yurnerix.financebot.service;

public interface AuditService {
    void logAction(long telegramId, String action, String details);
    void saveSnapshot(long telegramId, String totalIncome, String totalExpense, String balance,
                      String goalName, String targetAmount);
}