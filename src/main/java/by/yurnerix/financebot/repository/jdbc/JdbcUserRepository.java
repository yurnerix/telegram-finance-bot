package by.yurnerix.financebot.repository.jdbc;

import by.yurnerix.financebot.config.PostgresConnectionFactory;
import by.yurnerix.financebot.model.User;
import by.yurnerix.financebot.repository.UserRepository;

import java.sql.*;
import java.util.Optional;


public class JdbcUserRepository implements UserRepository {

    @Override
    public Optional<User> findByTelegramId(long telegramId) {
        String sql = "SELECT id, telegram_id, name, created_at FROM users WHERE telegram_id = ?";

        try (Connection connection = PostgresConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, telegramId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setTelegramId(rs.getLong("telegram_id"));
                    user.setName(rs.getString("name"));

                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        user.setCreatedAt(timestamp.toLocalDateTime());
                    }

                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя", e);
        }

        return Optional.empty();
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (telegram_id, name) VALUES (?, ?) RETURNING id, created_at";

        try (Connection connection = PostgresConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, user.getTelegramId());
            statement.setString(2, user.getName());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("id"));

                    Timestamp timestamp = rs.getTimestamp("created_at");
                    if (timestamp != null) {
                        user.setCreatedAt(timestamp.toLocalDateTime());
                    }
                }
            }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения пользователя", e);
        }
    }
}