package by.yurnerix.financebot.repository;

import by.yurnerix.financebot.model.Income;

import java.math.BigDecimal;
import java.util.List;

public interface IncomeRepository {
    Income save(Income income);
    List<Income> findByUserId(int userId);
    BigDecimal getTotalIncomeByUserId(int userId);
}
