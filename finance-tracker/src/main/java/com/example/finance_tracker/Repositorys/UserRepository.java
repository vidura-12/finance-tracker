package com.example.finance_tracker.Repositorys;

import com.example.finance_tracker.model.Budget;
import com.example.finance_tracker.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    //Optional<Budget> findByUserIdAndCategory(String userId, String category);
}