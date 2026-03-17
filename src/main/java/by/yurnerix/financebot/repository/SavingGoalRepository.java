package by.yurnerix.financebot.repository;

import by.yurnerix.financebot.model.SavingGoal;

import java.util.Optional;

public interface SavingGoalRepository {
    SavingGoal saveOrUpdate(SavingGoal goal);
    Optional<SavingGoal> findByUserId(int userId);
}
