package by.yurnerix.financebot.bot;

import by.yurnerix.financebot.bot.command.CommandHandler;
import by.yurnerix.financebot.bot.factory.CommandHandlerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public class FinanceTelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final CommandHandlerFactory commandHandlerFactory;

    public FinanceTelegramBot(String botToken, CommandHandlerFactory commandHandlerFactory) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.commandHandlerFactory = commandHandlerFactory;
    }

    @Override
    public void consume(Update update) {
        if (update == null || !update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String text = update.getMessage().getText().trim();
        long telegramId = update.getMessage().getFrom().getId();
        long chatId = update.getMessage().getChatId();

        String command = extractCommand(text);

        String response = commandHandlerFactory.findHandler(command)
                .map(handler -> handler.handle(telegramId, text))
                .orElse("Неизвестная команда.\nИспользуйте /help");

        sendMessage(chatId, response);
    }

    private String extractCommand(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }

        String[] parts = text.split("\\s+", 2);
        return parts[0];
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);

        try {
            telegramClient.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}