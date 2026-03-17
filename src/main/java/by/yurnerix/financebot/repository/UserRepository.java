package by.yurnerix.financebot.repository;

import by.yurnerix.financebot.model.User;

import java.util.Optional;


public interface UserRepository {
    Optional<User> findByTelegramId(long telegramId);
    User save(User user);
}