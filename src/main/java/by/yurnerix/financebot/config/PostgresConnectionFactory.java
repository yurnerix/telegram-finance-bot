package by.yurnerix.financebot.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class PostgresConnectionFactory {

    private PostgresConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                AppConfig.get("postgres.url"),
                AppConfig.get("postgres.user"),
                AppConfig.get("postgres.password")
        );
    }
}