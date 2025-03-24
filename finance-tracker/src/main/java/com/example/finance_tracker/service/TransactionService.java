package com.example.finance_tracker.service;

import com.example.finance_tracker.Repositorys.BudgetRepository;
import com.example.finance_tracker.Repositorys.FinancialGoalRepository;
import com.example.finance_tracker.Repositorys.TransactionRepository;
import com.example.finance_tracker.model.FinancialGoal;
import com.example.finance_tracker.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

public class TransactionService {

    private final TransactionRepository
            transactionRepository;
    private final CurrencyService currencyService;
    private final FinancialGoalRepository financialGoalRepository;
    public TransactionService(TransactionRepository transactionRepository, CurrencyService currencyService, FinancialGoalRepository financialGoalRepository) {
        this.transactionRepository = transactionRepository;
        this.currencyService = currencyService;
        this.financialGoalRepository = financialGoalRepository;
    }
    public List<Map<String, Object>> getExpenseTrends(String userId) {
        return transactionRepository.getExpenseTrends(userId);
    }

    public List<Map<String, Object>> getMonthlyExpenseReport(String userId) {
        return transactionRepository.getMonthlyExpenseReport(userId);
    }
    public Transaction createTransaction(Transaction transaction, String targetCurrency) {
        if (!transaction.getCurrency().equals(targetCurrency)) {
            BigDecimal exchangeRate = currencyService.getExchangeRate(transaction.getCurrency(), targetCurrency);
            BigDecimal convertedAmount = currencyService.convertAmount(transaction.getAmount(), transaction.getCurrency(), targetCurrency);

            transaction.setConvertedAmount(convertedAmount);
            transaction.setExchangeRate(exchangeRate);
            transaction.setCurrency(targetCurrency);
            transaction.setAmount(convertedAmount); // Update the amount to the converted amount
        }
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByUser(String userId, String targetCurrency) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        transactions.forEach(transaction -> {
            if (!transaction.getCurrency().equals(targetCurrency)) {
                BigDecimal convertedAmount = currencyService.convertAmount(transaction.getAmount(), transaction.getCurrency(), targetCurrency);
                transaction.setAmount(convertedAmount);
                transaction.setCurrency(targetCurrency);
            }
        });
        return transactions;
    }
    public Page<Transaction> getByUserIdAndFilters(String userId, String category, String type, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        if (category != null && type != null && startDate == null && endDate == null) {
            return transactionRepository.findByUserIdAndCategoryAndType(userId, category, type, pageable);
        }
        if (startDate != null && endDate != null && category != null && type != null) {
            return transactionRepository.findByUserIdAndDateBetween(userId, startDate, endDate, pageable);
        }
        if (category != null && type == null && startDate == null && endDate == null) {
            return transactionRepository.findByUserIdAndCategory(userId, category, pageable);
        }
        if (type != null && category == null && startDate == null && endDate == null) {
            return transactionRepository.findByUserIdAndType(userId, type, pageable);
        }
        if (startDate != null && endDate != null) {
            return transactionRepository.findByUserIdAndDateBetween(userId, startDate, endDate, pageable);
        }
        return transactionRepository.findByUserId(userId, pageable);
    }
    public Optional<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    public Transaction updateTransaction(String id, Transaction updatedTransaction) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transaction.setCategory(updatedTransaction.getCategory());
                    transaction.setAmount(updatedTransaction.getAmount());
                    transaction.setType(updatedTransaction.getType());
                    transaction.setDate(updatedTransaction.getDate());
                    transaction.setDescription(updatedTransaction.getDescription());
                    return transactionRepository.save(transaction);
                })
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);
    }
    public Map<String, BigDecimal> getTransactionSummary(String userId) {

        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("balance", totalIncome.subtract(totalExpense));

        return summary;
    }
    public List<Map<String, Object>> getIncomeVsExpenseTrends(String userId) {
        return transactionRepository.getIncomeVsExpenseTrends(userId);
    }
    public List<Transaction> getRecurringTransactions(String userId) {
        return transactionRepository.findByUserIdAndIsRecurringTrue(userId);
    }

    public void stopRecurringTransaction(String id) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(id);
        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();
            transaction.setRecurring(false);
            transactionRepository.save(transaction);
        }
    }
    private void allocateToGoals(String userId, BigDecimal incomeAmount) {
        List<FinancialGoal> goals = financialGoalRepository.findByUserId(userId);

        for (FinancialGoal goal : goals) {
            if (!goal.isCompleted()) {
                BigDecimal allocation = incomeAmount.multiply(new BigDecimal("0.10")); // Allocate 10%
                goal.setSavedAmount(goal.getSavedAmount().add(allocation));

                // Mark as completed if target is reached
                if (goal.getSavedAmount().compareTo(goal.getTargetAmount()) >= 0) {
                    goal.setCompleted(true);
                }

                financialGoalRepository.save(goal);
            }
        }
    }


}