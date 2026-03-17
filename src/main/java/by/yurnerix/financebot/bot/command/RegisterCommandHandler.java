package by.yurnerix.financebot.bot.command;

import by.yurnerix.financebot.service.FinanceService;

public class RegisterCommandHandler implements CommandHandler {

    private final FinanceService financeService;

    public RegisterCommandHandler(FinanceService financeService)
    {
        this.financeService = financeService;
    }

    @Override
    public boolean canHandle(String command)
    {
        return command.equals("/register");
    }

    @Override
    public String handle(long telegramId, String messageText) {
        String[] parts = messageText.split("\\s+", 2);

        if (parts.length < 2)
        {
            return "Формат: /register Имя";
        }

        String name = parts[1];
        return financeService.registerUser(telegramId, name);
    }

}
