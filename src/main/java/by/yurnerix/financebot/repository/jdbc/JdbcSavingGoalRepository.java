package by.yurnerix.financebot.repository.jdbc;

import by.yurnerix.financebot.config.PostgresConnectionFactory;
import by.yurnerix.financebot.model.SavingGoal;
import by.yurnerix.financebot.repository.SavingGoalRepository;

import java.sql.*;
import java.util.Optional;


public class JdbcSavingGoalRepository implements SavingGoalRepository {

    @Override
    public SavingGoal saveOrUpdate(SavingGoal goal) {
        String sql = """
                INSERT INTO saving_goals (user_id, target_amount, goal_name)
                VALUES (?, ?, ?)
                ON CONFLICT (user_id)
                DO UPDATE SET
                    target_amount = EXCLUDED.target_amount,
                    goal_name = EXCLUDED.goal_name
                RETURNING id, created_at
                """;

        try (Connection connection = PostgresConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, goal.getUserId());
            statement.setBigDecimal(2, goal.getTargetAmount());
            statement.setString(3, goal.getGoalName());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    goal.setId(rs.getInt("id"));

                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        goal.setCreatedAt(timestamp.toLocalDateTime());
                    }
                }
            }

            return goal;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения цели накопления", e);
        }
    }

    @Override
    public Optional<SavingGoal> findByUserId(int userId) {
        String sql = """
                SELECT id, user_id, target_amount, goal_name, created_at
                FROM saving_goals
                WHERE user_id = ?
                """;

        try (Connection connection = PostgresConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    SavingGoal goal = new SavingGoal();
                    goal.setId(rs.getInt("id"));
                    goal.setUserId(rs.getInt("user_id"));
                    goal.setTargetAmount(rs.getBigDecimal("target_amount"));
                    goal.setGoalName(rs.getString("goal_name"));

                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        goal.setCreatedAt(timestamp.toLocalDateTime());
                    }

                    return Optional.of(goal);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска цели накопления", e);
        }

        return Optional.empty();
    }
}