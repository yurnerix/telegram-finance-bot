package by.yurnerix.financebot.repository.jdbc;

import by.yurnerix.financebot.config.PostgresConnectionFactory;
import by.yurnerix.financebot.model.Expense;
import by.yurnerix.financebot.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JdbcExpenseRepository implements ExpenseRepository {

    @Override
    public Expense save(Expense expense) {
        String sql = """
                INSERT INTO expenses (user_id, amount, category, description)
                VALUES (?, ?, ?, ?)
                RETURNING id, created_at
                """;

        try (Connection connection = PostgresConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, expense.getUserId());
            statement.setBigDecimal(2, expense.getAmount());
            statement.setString(3, expense.getCategory());
            statement.setString(4, expense.getDescription());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    expense.setId(rs.getInt("id"));

                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        expense.setCreatedAt(timestamp.toLocalDateTime());
                    }
                }
            }

            return expense;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения расхода", e);
        }
    }

    @Override
    public List<Expense> findByUserId(int userId) {
        String sql = """
                SELECT id, user_id, amount, category, description, created_at
                FROM expenses
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;

        List<Expense> expenses = new ArrayList<>();

        try (Connection connection = PostgresConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense();
                    expense.setId(rs.getInt("id"));
                    expense.setUserId(rs.getInt("user_id"));
                    expense.setAmount(rs.getBigDecimal("amount"));
                    expense.setCategory(rs.getString("category"));
                    expense.setDescription(rs.getString("description"));

                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        expense.setCreatedAt(timestamp.toLocalDateTime());
                    }

                    expenses.add(expense);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения расходов", e);
        }

        return expenses;
    }

    @Override
    public BigDecimal getTotalExpenseByUserId(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total_expense FROM expenses WHERE user_id = ?";

        try (Connection connection = PostgresConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_expense");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подсчёта суммы расходов", e);
        }

        return BigDecimal.ZERO;
    }
}