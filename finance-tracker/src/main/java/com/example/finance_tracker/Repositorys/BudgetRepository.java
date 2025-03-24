package com.example.finance_tracker.Repositorys;

import com.example.finance_tracker.model.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends MongoRepository<Budget, String> {
    List<Budget> findByUserId(String userId);
    Optional<Budget> findByUserIdAndMonthAndYearAndCategory(String userId, int month, int year, String category);
    Optional<Budget> findByUserIdAndMonthAndYear(String userId, int month, int year);

    Optional<Budget> findByUserIdAndCategory(String userId, String category);
}
