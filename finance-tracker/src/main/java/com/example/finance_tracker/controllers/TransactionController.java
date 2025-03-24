package com.example.finance_tracker.controllers;

import com.example.finance_tracker.model.Transaction;
import com.example.finance_tracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")

public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody Transaction transaction,
            @RequestParam(defaultValue = "USD") String targetCurrency) {

        Transaction savedTransaction = transactionService.createTransaction(transaction, targetCurrency);
        return ResponseEntity.ok(savedTransaction);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<Page<Transaction>> getUserTransactions(
            @PathVariable String userId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(transactionService.getByUserIdAndFilters(userId, category, type, startDate, endDate, pageable));
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<Optional<Transaction>> getTransactionById(@PathVariable String id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id, @RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transaction));
    }
    @GetMapping("/{userId}/expense-trends")
    public ResponseEntity<List<Map<String, Object>>> getExpenseTrends(@PathVariable String userId) {
        return ResponseEntity.ok(transactionService.getExpenseTrends(userId));
    }
    @GetMapping("/{userId}/monthly-report")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyExpenseReport(@PathVariable String userId) {
        return ResponseEntity.ok(transactionService.getMonthlyExpenseReport(userId));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction deleted successfully");
    }
    @GetMapping("/{userId}/summary")
    public ResponseEntity<Map<String, BigDecimal>> getTransactionSummary(@PathVariable String userId) {
        return ResponseEntity.ok(transactionService.getTransactionSummary(userId));
    }
    @GetMapping("/{userId}/income-expense-trends")
    public ResponseEntity<List<Map<String, Object>>> getIncomeVsExpenseTrends(@PathVariable String userId) {
        return ResponseEntity.ok(transactionService.getIncomeVsExpenseTrends(userId));
    }
    // 🔥 Get Recurring Transactions for a User

    @GetMapping("/recurring/{userId}")
    public ResponseEntity<List<Transaction>> getRecurringTransactions(@PathVariable String userId) {
        return ResponseEntity.ok(transactionService.getRecurringTransactions(userId));
    }

    @PutMapping("/recurring/{id}/stop")
    public ResponseEntity<String> stopRecurringTransaction(@PathVariable String id) {
        transactionService.stopRecurringTransaction(id);
        return ResponseEntity.ok("Recurring transaction stopped.");
    }
}