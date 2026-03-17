package by.yurnerix.financebot.observer;

public interface FinanceEventListener {
    void onEvent(long telegramId, String eventType, String details);
}
