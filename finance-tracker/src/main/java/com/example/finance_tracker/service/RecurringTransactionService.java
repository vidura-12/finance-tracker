package com.example.finance_tracker.service;

import com.example.finance_tracker.Repositorys.TransactionRepository;
import com.example.finance_tracker.model.Transaction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RecurringTransactionService {
    private final TransactionRepository transactionRepository;

    public RecurringTransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // Runs daily at midnight
    public void processRecurringTransactions() {
        LocalDateTime now = LocalDateTime.now();
        List<Transaction> recurringTransactions = transactionRepository.findByIsRecurringTrue();

        for (Transaction transaction : recurringTransactions) {
            // Skip transactions that have ended
            if (transaction.getEndDate() != null && transaction.getEndDate().isBefore(now)) {
                continue;
            }

            // Calculate the next transaction date
            LocalDateTime nextTransactionDate = getNextTransactionDate(transaction.getDate(), transaction.getRecurrencePattern());

            // Ensure we don't insert duplicate transactions for the same date
            boolean exists = transactionRepository.existsByUserIdAndCategoryAndDate(
                    transaction.getUserId(), transaction.getCategory(), nextTransactionDate);

            if (!exists && nextTransactionDate.isBefore(now)) {
                // Create a new transaction for the next occurrence
                Transaction newTransaction = new Transaction(
                        transaction.getAmount(),
                        transaction.getCategory(),
                        transaction.getCurrency(),
                        transaction.getDate(),
                        transaction.getEndDate(),
                        transaction.getId(),
                        transaction.getRecurrencePattern(),
                        transaction.getUserId(),
                        transaction.getType(),
                        true,
                        transaction.getExchangeRate(),
                        transaction.getDescription(),
                        transaction.getConvertedAmount()
                );


                // Save and notify the user
                transactionRepository.save(newTransaction);
                sendNotification(transaction.getUserId(), newTransaction);
            }
        }
    }


    private LocalDateTime getNextTransactionDate(LocalDateTime lastDate, String recurrencePattern) {
        switch (recurrencePattern.toUpperCase()) {
            case "DAILY":
                return lastDate.plus(1, ChronoUnit.DAYS);
            case "WEEKLY":
                return lastDate.plus(1, ChronoUnit.WEEKS);
            case "MONTHLY":
                return lastDate.plus(1, ChronoUnit.MONTHS);
            default:
                throw new IllegalArgumentException("Invalid recurrence pattern: " + recurrencePattern);
        }
    }

    private void sendNotification(String userId, Transaction transaction) {
        System.out.println("Reminder: Upcoming Transaction for user " + userId + ": " + transaction.getDescription());
    }
}
