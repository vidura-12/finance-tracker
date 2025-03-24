package com.example.finance_tracker.controllers;

import com.example.finance_tracker.Repositorys.TransactionRepository;
import com.example.finance_tracker.dto.UserResponseDTO;
import com.example.finance_tracker.model.Role;
import com.example.finance_tracker.model.Transaction;
import com.example.finance_tracker.model.User;
import com.example.finance_tracker.Repositorys.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
 // Only admins can access!
public class AdminController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    public AdminController(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Promote user to admin
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable String id, @RequestParam String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Convert the string role to Role enum
        Role newRole = Role.valueOf(role.toUpperCase());

        Set<Role> roles = user.getRoles();
        roles.add(newRole);
        user.setRoles(roles);

        userRepository.save(user);
        return ResponseEntity.ok("User role updated to " + role);
    }

    // Delete a user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // System overview: total users and more
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverview() {
        // Total users
        long totalUsers = userRepository.count();

        // Total transactions
        List<Transaction> allTransactions = transactionRepository.findAll();
        long totalTransactions = allTransactions.size();

        // Calculate total income and expense
        BigDecimal totalIncome = allTransactions.stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType())) // Filter by type = INCOME
                .map(Transaction::getAmount) // Extract the amount
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum all amounts

        BigDecimal totalExpense = allTransactions.stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType())) // Filter by type = EXPENSE
                .map(Transaction::getAmount) // Extract the amount
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum all amounts

        // Build the response
        Map<String, Object> overview = new HashMap<>();
        overview.put("totalUsers", totalUsers);
        overview.put("totalTransactions", totalTransactions);
        overview.put("totalIncome", totalIncome);
        overview.put("totalExpense", totalExpense);

        return ResponseEntity.ok(overview);
    }
}
