package by.yurnerix.financebot.bot.factory;

import by.yurnerix.financebot.bot.command.CommandHandler;

import java.util.List;
import java.util.Optional;


public class CommandHandlerFactory {

    private final List<CommandHandler> handlers;

    public CommandHandlerFactory(List<CommandHandler> handlers) {
        this.handlers = handlers;
    }

    public Optional<CommandHandler> findHandler(String command) {
        return handlers.stream()
                .filter(handler -> handler.canHandle(command))
                .findFirst();
    }
}