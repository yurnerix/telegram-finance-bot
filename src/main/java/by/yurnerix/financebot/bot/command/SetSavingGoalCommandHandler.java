package by.yurnerix.financebot.bot.command;

import by.yurnerix.financebot.service.FinanceService;


public class SetSavingGoalCommandHandler implements CommandHandler {

    private final FinanceService financeService;

    public SetSavingGoalCommandHandler(FinanceService financeService) {
        this.financeService = financeService;
    }

    @Override
    public boolean canHandle(String command) {
        return "/setsavinggoal".equals(command);
    }

    @Override
    public String handle(long telegramId, String messageText) {
        String[] parts = messageText.trim().split("\\s+", 3);

        if (parts.length < 3) {
            return "Формат команды: /setsavinggoal Сумма Название";
        }

        String amountText = parts[1];
        String goalName = parts[2];

        return financeService.setSavingGoal(telegramId, amountText, goalName);
    }
}