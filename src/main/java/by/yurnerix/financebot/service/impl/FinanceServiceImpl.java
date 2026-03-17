package by.yurnerix.financebot.service.impl;

import by.yurnerix.financebot.model.*;
import by.yurnerix.financebot.observer.FinanceEventPublisher;
import by.yurnerix.financebot.repository.ExpenseRepository;
import by.yurnerix.financebot.repository.IncomeRepository;
import by.yurnerix.financebot.repository.SavingGoalRepository;
import by.yurnerix.financebot.repository.UserRepository;
import by.yurnerix.financebot.service.AuditService;
import by.yurnerix.financebot.service.FinanceService;
import by.yurnerix.financebot.util.FinanceSummaryBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;


public class FinanceServiceImpl implements FinanceService {

    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final SavingGoalRepository savingGoalRepository;
    private final AuditService auditService;
    private final FinanceEventPublisher eventPublisher;

    public FinanceServiceImpl(UserRepository userRepository,
                              IncomeRepository incomeRepository,
                              ExpenseRepository expenseRepository,
                              SavingGoalRepository savingGoalRepository,
                              AuditService auditService,
                              FinanceEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.savingGoalRepository = savingGoalRepository;
        this.auditService = auditService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String registerUser(long telegramId, String name) {
        if (name == null || name.isBlank()) {
            return "Имя не должно быть пустым.";
        }

        Optional<User> existingUser = userRepository.findByTelegramId(telegramId);
        if (existingUser.isPresent()) {
            return "Вы уже зарегистрированы.";
        }

        User user = new User(telegramId, name.trim());
        userRepository.save(user);

        eventPublisher.publish(telegramId, "REGISTER", "Пользователь зарегистрирован: " + user.getName());

        return "Регистрация прошла успешно.\nИмя: " + user.getName();
    }

    @Override
    public String addIncome(long telegramId, String amountText, String source) {
        User user = getRegisteredUserOrNull(telegramId);
        if (user == null) {
            return "Сначала зарегистрируйтесь через /register Имя";
        }

        if (source == null || source.isBlank()) {
            return "Источник дохода не должен быть пустым.";
        }

        BigDecimal amount = parsePositiveAmount(amountText);
        if (amount == null) {
            return "Некорректная сумма дохода.";
        }

        Income income = new Income(user.getId(), amount, source.trim());
        incomeRepository.save(income);

        eventPublisher.publish(telegramId, "ADD_INCOME",
                "Добавлен доход: " + amount + ", источник: " + source.trim());

        return "Доход успешно добавлен.\nСумма: " + amount + "\nИсточник: " + source.trim();
    }

    @Override
    public String addExpense(long telegramId, String amountText, String category, String description) {
        User user = getRegisteredUserOrNull(telegramId);
        if (user == null) {
            return "Сначала зарегистрируйтесь через /register Имя";
        }

        if (category == null || category.isBlank()) {
            return "Категория расхода не должна быть пустой.";
        }

        BigDecimal amount = parsePositiveAmount(amountText);
        if (amount == null) {
            return "Некорректная сумма расхода.";
        }

        String safeDescription = (description == null || description.isBlank())
                ? "Без описания"
                : description.trim();

        Expense expense = new Expense(user.getId(), amount, category.trim(), safeDescription);
        expenseRepository.save(expense);

        eventPublisher.publish(telegramId, "ADD_EXPENSE",
                "Добавлен расход: " + amount + ", категория: " + category.trim());

        return "Расход успешно добавлен.\nСумма: " + amount +
                "\nКатегория: " + category.trim() +
                "\nОписание: " + safeDescription;
    }

    @Override
    public String getBalance(long telegramId) {
        User user = getRegisteredUserOrNull(telegramId);
        if (user == null) {
            return "Сначала зарегистрируйтесь через /register Имя";
        }

        BigDecimal totalIncome = incomeRepository.getTotalIncomeByUserId(user.getId());
        BigDecimal totalExpense = expenseRepository.getTotalExpenseByUserId(user.getId());
        BigDecimal balance = totalIncome.subtract(totalExpense);

        auditService.saveSnapshot(
                telegramId,
                totalIncome.toPlainString(),
                totalExpense.toPlainString(),
                balance.toPlainString(),
                "Не установлена",
                "0"
        );

        eventPublisher.publish(telegramId, "VIEW_BALANCE", "Пользователь посмотрел баланс");

        return "Ваш баланс:\n" +
                "Доходы: " + totalIncome + "\n" +
                "Расходы: " + totalExpense + "\n" +
                "Остаток: " + balance;
    }

    @Override
    public String setSavingGoal(long telegramId, String amountText, String goalName) {
        User user = getRegisteredUserOrNull(telegramId);
        if (user == null) {
            return "Сначала зарегистрируйтесь через /register Имя";
        }

        if (goalName == null || goalName.isBlank()) {
            return "Название цели не должно быть пустым.";
        }

        BigDecimal targetAmount = parsePositiveAmount(amountText);
        if (targetAmount == null) {
            return "Некорректная сумма цели накопления.";
        }

        SavingGoal goal = new SavingGoal(user.getId(), targetAmount, goalName.trim());
        savingGoalRepository.saveOrUpdate(goal);

        eventPublisher.publish(telegramId, "SET_SAVING_GOAL",
                "Установлена цель: " + goalName.trim() + ", сумма: " + targetAmount);

        return "Цель накопления сохранена.\nЦель: " + goalName.trim() +
                "\nСумма: " + targetAmount;
    }

    @Override
    public String getSummary(long telegramId) {
        User user = getRegisteredUserOrNull(telegramId);
        if (user == null) {
            return "Сначала зарегистрируйтесь через /register Имя";
        }

        BigDecimal totalIncome = incomeRepository.getTotalIncomeByUserId(user.getId());
        BigDecimal totalExpense = expenseRepository.getTotalExpenseByUserId(user.getId());
        BigDecimal balance = totalIncome.subtract(totalExpense);

        Optional<SavingGoal> goalOptional = savingGoalRepository.findByUserId(user.getId());

        String goalName = "Не установлена";
        BigDecimal targetAmount = BigDecimal.ZERO;
        BigDecimal remainingToSave = BigDecimal.ZERO;

        if (goalOptional.isPresent()) {
            SavingGoal goal = goalOptional.get();
            goalName = goal.getGoalName();
            targetAmount = goal.getTargetAmount();

            remainingToSave = targetAmount.subtract(balance);
            if (remainingToSave.compareTo(BigDecimal.ZERO) < 0) {
                remainingToSave = BigDecimal.ZERO;
            }
        }

        FinanceSummary summary = new FinanceSummaryBuilder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .goalName(goalName)
                .targetAmount(targetAmount)
                .remainingToSave(remainingToSave)
                .build();

        auditService.saveSnapshot(
                telegramId,
                summary.getTotalIncome().toPlainString(),
                summary.getTotalExpense().toPlainString(),
                summary.getBalance().toPlainString(),
                summary.getGoalName(),
                summary.getTargetAmount().toPlainString()
        );

        eventPublisher.publish(telegramId, "VIEW_SUMMARY", "Пользователь запросил сводку");

        BigDecimal recommendedToSave = BigDecimal.ZERO;
        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            recommendedToSave = balance.multiply(new BigDecimal("0.20"))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return "Финансовая сводка:\n" +
                "Доходы: " + summary.getTotalIncome() + "\n" +
                "Расходы: " + summary.getTotalExpense() + "\n" +
                "Остаток: " + summary.getBalance() + "\n" +
                "Цель: " + summary.getGoalName() + "\n" +
                "Нужно накопить: " + summary.getTargetAmount() + "\n" +
                "Осталось накопить: " + summary.getRemainingToSave() + "\n" +
                "Рекомендуется отложить сейчас: " + recommendedToSave;
    }

    @Override
    public String getHelp() {
        return """
            Доступные команды бота:
            
            /start
            Запускает бота и выводит приветственное сообщение.
            Используйте эту команду, если открыли бота впервые.
            
            /help
            Показывает подробную справку по всем доступным командам.
            
            /register Имя
            Регистрирует пользователя в системе.
            Без регистрации остальные финансовые команды работать не будут.
            Пример:
            /register Yura
            
            /addincome Сумма Источник
            Добавляет доход пользователя.
            Где:
            - Сумма — число больше 0
            - Источник — откуда поступили деньги
            Пример:
            /addincome 50000 зарплата
            /addincome 12000 подработка
            
            /addexpense Сумма Категория Описание
            Добавляет расход пользователя.
            Где:
            - Сумма — число больше 0
            - Категория — тип расхода
            - Описание — пояснение, на что были потрачены деньги
            Пример:
            /addexpense 2500 продукты супермаркет
            /addexpense 800 транспорт такси
            
            /balance
            Показывает текущий финансовый баланс:
            - общую сумму доходов
            - общую сумму расходов
            - остаток денег
            
            /setsavinggoal Сумма Название
            Устанавливает или обновляет цель накопления.
            Где:
            - Сумма — сколько нужно накопить
            - Название — название цели
            Пример:
            /setsavinggoal 100000 отпуск
            /setsavinggoal 80000 ноутбук
            
            /summary
            Показывает полную финансовую сводку:
            - сумму всех доходов
            - сумму всех расходов
            - текущий остаток
            - цель накопления
            - сколько осталось накопить
            - рекомендуемую сумму для откладывания
            
            Важные правила:
            1. Сначала выполните регистрацию через /register Имя
            2. Суммы вводите только числами
            3. В названиях и описаниях можно использовать слова через пробел
            4. Если бот пишет, что формат неверный, проверьте пример команды
            
            Пример порядка работы:
            1. /register Yura
            2. /addincome 50000 зарплата
            3. /addexpense 3000 продукты магазин
            4. /balance
            5. /setsavinggoal 100000 отпуск
            6. /summary
            """;
    }

    private User getRegisteredUserOrNull(long telegramId) {
        return userRepository.findByTelegramId(telegramId).orElse(null);
    }

    private BigDecimal parsePositiveAmount(String amountText) {
        if (amountText == null || amountText.isBlank()) {
            return null;
        }

        try {
            BigDecimal amount = new BigDecimal(amountText.replace(",", "."));
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                return null;
            }
            return amount;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}