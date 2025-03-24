package com.example.finance_tracker.Repositorys;

import com.example.finance_tracker.model.FinancialGoal;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface FinancialGoalRepository extends MongoRepository<FinancialGoal, String> {
    List<FinancialGoal> findByUserId(String userId);
}
