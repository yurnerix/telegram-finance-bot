package by.yurnerix.financebot.bot.command;

import by.yurnerix.financebot.service.FinanceService;


public class HelpCommandHandler implements CommandHandler {

    private final FinanceService financeService;

    public HelpCommandHandler(FinanceService financeService) {
        this.financeService = financeService;
    }

    @Override
    public boolean canHandle(String command) {
        return "/help".equals(command) || "/start".equals(command);
    }

    @Override
    public String handle(long telegramId, String messageText) {
        return "Привет, меня зовут Юрий! Я бот управления личными финансами.\n\n" + financeService.getHelp();
    }
}