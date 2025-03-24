package com.example.finance_tracker.controllers;

import com.example.finance_tracker.model.FinancialGoal;
import com.example.finance_tracker.service.FinancialGoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class FinancialGoalController {

    private final FinancialGoalService goalService;

    public FinancialGoalController(FinancialGoalService goalService) {
        this.goalService = goalService;
    }

    // 1️⃣ Create a financial goal
    @PostMapping
    public ResponseEntity<FinancialGoal> createGoal(@RequestBody FinancialGoal goal) {
        return ResponseEntity.ok(goalService.createGoal(goal));
    }

    // 2️⃣ Get all goals for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<FinancialGoal>> getUserGoals(@PathVariable String userId) {
        return ResponseEntity.ok(goalService.getUserGoals(userId));
    }

    // 3️⃣ Update goal progress (saving money)
    @PutMapping("/{goalId}/save")
    public ResponseEntity<FinancialGoal> updateGoalProgress(@PathVariable String goalId,
                                                            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(goalService.updateGoalProgress(goalId, amount));
    }

    // 4️⃣ Delete a goal
    @DeleteMapping("/{goalId}")
    public ResponseEntity<String> deleteGoal(@PathVariable String goalId) {
        goalService.deleteGoal(goalId);
        return ResponseEntity.ok("Goal deleted successfully");
    }
    // Pause goal
    @PutMapping("/{goalId}/pause")
    public ResponseEntity<FinancialGoal> pauseGoal(@PathVariable String goalId) {
        return ResponseEntity.ok(goalService.pauseGoal(goalId));
    }

    // Resume goal
    @PutMapping("/{goalId}/resume")
    public ResponseEntity<FinancialGoal> resumeGoal(@PathVariable String goalId) {
        return ResponseEntity.ok(goalService.resumeGoal(goalId));
    }

}
