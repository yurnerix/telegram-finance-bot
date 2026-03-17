package by.yurnerix.financebot.repository;

import by.yurnerix.financebot.model.Expense;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseRepository {
    Expense save(Expense expense);
    List<Expense> findByUserId(int userId);
    BigDecimal getTotalExpenseByUserId(int userId);
}
