package com.example.finance_tracker.service;

import com.example.finance_tracker.Repositorys.BudgetRepository;
import com.example.finance_tracker.model.Budget;
import com.example.finance_tracker.model.Transaction;
import com.example.finance_tracker.Repositorys.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service

public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final EmailService emailService;  // Inject EmailService

    public BudgetService(BudgetRepository budgetRepository, TransactionRepository transactionRepository, EmailService emailService) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
        this.emailService = emailService;
    }
    /**
     * Set a new budget for a user.
     */
    public Budget setBudget(String userId, String category, BigDecimal limitAmount, int month, int year) {
        Budget budget = new Budget(userId, category, limitAmount, month, year);
        return budgetRepository.save(budget);
    }


    /**
     * Get a user's budget for a specific month and year.
     */
    public Optional<Budget> getBudget(String userId, String category, int month, int year) {
        return budgetRepository.findByUserIdAndMonthAndYearAndCategory(userId, month, year, category);
    }

    /**
     * Get all budgets for a user.
     */
    public List<Budget> getUserBudgets(String userId) {
        return budgetRepository.findByUserId(userId);
    }

    /**
     * Check if the user is nearing or exceeding the budget.
     */
    public String checkBudgetStatus(String userId, String category, BigDecimal spentAmount, String userEmail) {
        Optional<Budget> budgetOpt = budgetRepository.findByUserIdAndCategory(userId, category);
        if (budgetOpt.isEmpty()) {
            return "❌ No budget set for category: " + category;
        }

        Budget budget = budgetOpt.get();
        BigDecimal limit = budget.getLimitAmount();
        double spent = spentAmount.doubleValue();
        double budgetLimit = limit.doubleValue();

        if (spent >= 0.8 * budgetLimit) {
            emailService.sendBudgetWarning(userEmail, category, userId, spent, budgetLimit);
            return "⚠ Warning! You have reached 80% of your budget.";
        } else if (spent > budgetLimit) {
            emailService.sendBudgetWarning(userEmail, category, userId, spent, budgetLimit);
            return "❌ You have EXCEEDED your budget!";
        }

        return "✅ Budget is within limits.";
    }
    public String suggestBudgetAdjustment(String userId, String category) {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDate startOfMonth = lastMonth.atDay(1);
        LocalDate endOfMonth = lastMonth.atEndOfMonth();

        List<Transaction> pastTransactions = transactionRepository.findByUserIdAndCategoryAndDateBetween(userId, category, startOfMonth.atStartOfDay(), endOfMonth.atTime(23, 59, 59));
        BigDecimal totalSpent = pastTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Optional<Budget> budgetOpt = budgetRepository.findByUserIdAndCategory(userId, category);
        if (budgetOpt.isEmpty()) {
            return "❌ No budget set for category: " + category;
        }

        Budget budget = budgetOpt.get();
        BigDecimal currentLimit = budget.getLimitAmount();

        if (totalSpent.compareTo(currentLimit.multiply(new BigDecimal("0.9"))) > 0) {
            return "⚠ Consider increasing your budget for " + category + " to $" + totalSpent.multiply(new BigDecimal("1.1"));
        } else if (totalSpent.compareTo(currentLimit.multiply(new BigDecimal("0.5"))) < 0) {
            return "✅ You are spending less than expected. Consider reducing your budget for " + category + " to $" + totalSpent.multiply(new BigDecimal("0.9"));
        } else {
            return "✔ Your budget for " + category + " is well-balanced!";
        }
    }
    /**
     * Provide budget adjustment recommendations.
     */
    public String recommendBudgetAdjustments(String userId, String category) {
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        Optional<Budget> budgetOpt = budgetRepository.findByUserIdAndMonthAndYearAndCategory(userId, currentMonth, currentYear, category);

        if (budgetOpt.isEmpty()) {
            return "No budget set for category: " + category;
        }

        Budget budget = budgetOpt.get();
        BigDecimal limit = budget.getLimitAmount();

        // Fix: Add Pageable parameter
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        BigDecimal totalSpent = transactionRepository.findByUserIdAndCategory(userId, category, pageable)
                .getContent()
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalSpent.compareTo(limit) > 0) {
            return "Consider increasing your budget for " + category + " to accommodate higher spending.";
        } else if (totalSpent.compareTo(limit.multiply(BigDecimal.valueOf(0.5))) < 0) {
            return "You are spending significantly less on " + category + ". Consider adjusting your budget accordingly.";
        }

        return "Your budget for " + category + " is well balanced.";
    }
}
