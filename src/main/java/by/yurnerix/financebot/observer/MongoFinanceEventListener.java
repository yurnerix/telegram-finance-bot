package by.yurnerix.financebot.observer;

import by.yurnerix.financebot.service.AuditService;

public class MongoFinanceEventListener implements FinanceEventListener {

    private final AuditService auditService;

    public MongoFinanceEventListener(AuditService auditService)
    {
        this.auditService = auditService;
    }

    @Override
    public void onEvent(long telegramId, String eventType, String details) {
        auditService.logAction(telegramId, eventType, details);
    }


}
