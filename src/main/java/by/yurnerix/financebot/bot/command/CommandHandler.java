package by.yurnerix.financebot.bot.command;

public interface CommandHandler {
    boolean canHandle(String command);
    String handle(long telegramId, String messageText);
}
