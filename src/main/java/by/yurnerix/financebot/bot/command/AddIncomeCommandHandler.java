package by.yurnerix.financebot.bot.command;

import by.yurnerix.financebot.service.FinanceService;


public class AddIncomeCommandHandler implements CommandHandler {

    private final FinanceService financeService;

    public AddIncomeCommandHandler(FinanceService financeService) {
        this.financeService = financeService;
    }

    @Override
    public boolean canHandle(String command) {
        return "/addincome".equals(command);
    }

    @Override
    public String handle(long telegramId, String messageText) {
        String[] parts = messageText.trim().split("\\s+", 3);

        if (parts.length < 3) {
            return "Формат команды: /addincome Сумма Источник";
        }

        String amountText = parts[1];
        String source = parts[2];

        return financeService.addIncome(telegramId, amountText, source);
    }
}