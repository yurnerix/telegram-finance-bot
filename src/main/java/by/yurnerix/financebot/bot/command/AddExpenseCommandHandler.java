package by.yurnerix.financebot.bot.command;

import by.yurnerix.financebot.service.FinanceService;


public class AddExpenseCommandHandler implements CommandHandler {

    private final FinanceService financeService;

    public AddExpenseCommandHandler(FinanceService financeService) {
        this.financeService = financeService;
    }

    @Override
    public boolean canHandle(String command) {
        return "/addexpense".equals(command);
    }

    @Override
    public String handle(long telegramId, String messageText) {
        String[] parts = messageText.trim().split("\\s+", 4);

        if (parts.length < 3) {
            return "Формат команды: /addexpense Сумма Категория Описание";
        }

        String amountText = parts[1];
        String category = parts[2];
        String description = parts.length >= 4 ? parts[3] : "Без описания";

        return financeService.addExpense(telegramId, amountText, category, description);
    }
}