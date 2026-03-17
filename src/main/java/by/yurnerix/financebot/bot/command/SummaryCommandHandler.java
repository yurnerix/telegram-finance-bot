package by.yurnerix.financebot.bot.command;

import by.yurnerix.financebot.service.FinanceService;


public class SummaryCommandHandler implements CommandHandler {

    private final FinanceService financeService;

    public SummaryCommandHandler(FinanceService financeService) {
        this.financeService = financeService;
    }

    @Override
    public boolean canHandle(String command) {
        return "/summary".equals(command);
    }

    @Override
    public String handle(long telegramId, String messageText) {
        return financeService.getSummary(telegramId);
    }
}