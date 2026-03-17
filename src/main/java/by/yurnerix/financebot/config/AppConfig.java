package by.yurnerix.financebot.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = AppConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("Файл application.properties не найден");
            }

            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки application.properties", e);
        }
    }

    private AppConfig() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
