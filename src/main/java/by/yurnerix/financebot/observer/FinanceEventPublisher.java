package by.yurnerix.financebot.observer;

import java.util.ArrayList;
import java.util.List;

public class FinanceEventPublisher {

    private final List<FinanceEventListener> listeners = new ArrayList<>();

    public void addListener(FinanceEventListener listener)
    {
        listeners.add(listener);
    }

    public void publish(long telegramId, String eventType, String details) {
        for (FinanceEventListener listener : listeners)
        {
            listener.onEvent(telegramId, eventType, details);
        }
    }

}
