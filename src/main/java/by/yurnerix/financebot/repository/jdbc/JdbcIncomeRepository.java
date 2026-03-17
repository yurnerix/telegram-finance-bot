package by.yurnerix.financebot.repository.jdbc;

import by.yurnerix.financebot.config.PostgresConnectionFactory;
import by.yurnerix.financebot.model.Income;
import by.yurnerix.financebot.repository.IncomeRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JdbcIncomeRepository implements IncomeRepository {

    @Override
    public Income save(Income income) {
        String sql = """
                INSERT INTO incomes (user_id, amount, source)
                VALUES (?, ?, ?)
                RETURNING id, created_at
                """;

        try (Connection connection = PostgresConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, income.getUserId());
            statement.setBigDecimal(2, income.getAmount());
            statement.setString(3, income.getSource());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    income.setId(rs.getInt("id"));

                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        income.setCreatedAt(timestamp.toLocalDateTime());
                    }
                }
            }

            return income;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения дохода", e);
        }
    }

    @Override
    public List<Income> findByUserId(int userId) {
        String sql = """
                SELECT id, user_id, amount, source, created_at
                FROM incomes
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;

        List<Income> incomes = new ArrayList<>();

        try (Connection connection = PostgresConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Income income = new Income();
                    income.setId(rs.getInt("id"));
                    income.setUserId(rs.getInt("user_id"));
                    income.setAmount(rs.getBigDecimal("amount"));
                    income.setSource(rs.getString("source"));

                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        income.setCreatedAt(timestamp.toLocalDateTime());
                    }

                    incomes.add(income);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения доходов", e);
        }

        return incomes;
    }

    @Override
    public BigDecimal getTotalIncomeByUserId(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total_income FROM incomes WHERE user_id = ?";

        try (Connection connection = PostgresConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_income");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подсчёта суммы доходов", e);
        }

        return BigDecimal.ZERO;
    }
}