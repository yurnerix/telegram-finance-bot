package by.yurnerix.financebot.bot.command;

import by.yurnerix.financebot.service.FinanceService;

public class BalanceCommandHandler implements CommandHandler {

    private final FinanceService financeService;

    public BalanceCommandHandler(FinanceService financeService) {
        this.financeService = financeService;
    }

    @Override
    public boolean canHandle(String command) {
        return command.equals("/balance");
    }

    @Override
    public String handle(long telegramId, String messageText) {
        return financeService.getBalance(telegramId);
    }
}