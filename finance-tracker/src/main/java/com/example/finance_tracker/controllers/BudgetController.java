package com.example.finance_tracker.controllers;

import com.example.finance_tracker.model.Budget;
import com.example.finance_tracker.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // ✅ Set a budget
    @PostMapping("/{userId}")
    public ResponseEntity<Budget> setBudget(
            @PathVariable String userId,
            @RequestParam String category,
            @RequestParam BigDecimal limitAmount) {

        LocalDate today = LocalDate.now();  // Get current month & year
        int month = today.getMonthValue();
        int year = today.getYear();

        return ResponseEntity.ok(budgetService.setBudget(userId, category, limitAmount, month, year));
    }

    // ✅ Get budget details
    @GetMapping("/{userId}")
    public ResponseEntity<List<Budget>> getUserBudgets(@PathVariable String userId) {
        return ResponseEntity.ok(budgetService.getUserBudgets(userId));
    }

    @GetMapping("/{userId}/status")
    public ResponseEntity<String> checkBudgetStatus(
            @PathVariable String userId,
            @RequestParam String category,
            @RequestParam BigDecimal spentAmount,
            @RequestParam String email) {  // Accept user email
        return ResponseEntity.ok(budgetService.checkBudgetStatus(userId, category, spentAmount, email));
    }
    // ✅ Get recommendations for adjusting budget
    @GetMapping("/{userId}/recommendations")
    public ResponseEntity<String> recommendBudgetAdjustments(
            @PathVariable String userId,
            @RequestParam String category) {
        return ResponseEntity.ok(budgetService.recommendBudgetAdjustments(userId, category));
    }
    @GetMapping("/{userId}/adjustment")
    public ResponseEntity<String> suggestBudgetAdjustment(
            @PathVariable String userId,
            @RequestParam String category) {
        return ResponseEntity.ok(budgetService.suggestBudgetAdjustment(userId, category));
    }
}
