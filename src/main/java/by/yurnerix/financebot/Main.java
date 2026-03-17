package by.yurnerix.financebot;

import by.yurnerix.financebot.bot.FinanceTelegramBot;
import by.yurnerix.financebot.bot.command.*;
import by.yurnerix.financebot.bot.factory.CommandHandlerFactory;
import by.yurnerix.financebot.config.AppConfig;
import by.yurnerix.financebot.observer.FinanceEventPublisher;
import by.yurnerix.financebot.observer.MongoFinanceEventListener;
import by.yurnerix.financebot.repository.ExpenseRepository;
import by.yurnerix.financebot.repository.IncomeRepository;
import by.yurnerix.financebot.repository.SavingGoalRepository;
import by.yurnerix.financebot.repository.UserRepository;
import by.yurnerix.financebot.repository.jdbc.JdbcExpenseRepository;
import by.yurnerix.financebot.repository.jdbc.JdbcIncomeRepository;
import by.yurnerix.financebot.repository.jdbc.JdbcSavingGoalRepository;
import by.yurnerix.financebot.repository.jdbc.JdbcUserRepository;
import by.yurnerix.financebot.service.AuditService;
import by.yurnerix.financebot.service.FinanceService;
import by.yurnerix.financebot.service.impl.FinanceServiceImpl;
import by.yurnerix.financebot.service.impl.MongoAuditService;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            UserRepository userRepository = new JdbcUserRepository();
            IncomeRepository incomeRepository = new JdbcIncomeRepository();
            ExpenseRepository expenseRepository = new JdbcExpenseRepository();
            SavingGoalRepository savingGoalRepository = new JdbcSavingGoalRepository();

            AuditService auditService = new MongoAuditService();

            FinanceEventPublisher eventPublisher = new FinanceEventPublisher();
            eventPublisher.addListener(new MongoFinanceEventListener(auditService));

            FinanceService financeService = new FinanceServiceImpl(
                    userRepository,
                    incomeRepository,
                    expenseRepository,
                    savingGoalRepository,
                    auditService,
                    eventPublisher
            );

            List<CommandHandler> handlers = List.of(
                    new HelpCommandHandler(financeService),
                    new RegisterCommandHandler(financeService),
                    new AddIncomeCommandHandler(financeService),
                    new AddExpenseCommandHandler(financeService),
                    new BalanceCommandHandler(financeService),
                    new SetSavingGoalCommandHandler(financeService),
                    new SummaryCommandHandler(financeService)
            );

            CommandHandlerFactory commandHandlerFactory = new CommandHandlerFactory(handlers);

            String botToken = AppConfig.get("bot.token");

            FinanceTelegramBot bot = new FinanceTelegramBot(botToken, commandHandlerFactory);

            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(botToken, bot);

            System.out.println("Telegram-бот успешно запущен.");
            Thread.currentThread().join();

        } catch (Exception e) {
            System.out.println("Ошибка запуска Telegram-бота:");
            e.printStackTrace();
        }
    }
}