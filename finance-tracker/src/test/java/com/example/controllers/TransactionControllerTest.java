package com.example.controllers;
import com.example.finance_tracker.controllers.TransactionController;
import com.example.finance_tracker.model.Transaction;
import com.example.finance_tracker.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
        transaction.setId("1");
        transaction.setUserId("testuser");
        transaction.setCategory("Food");
        transaction.setAmount(BigDecimal.valueOf(50.00));
        transaction.setType("EXPENSE");
        transaction.setDate(LocalDateTime.now());
    }

    // ✅ Test for createTransaction
    @Test
    public void testCreateTransaction() {
        // Arrange
        when(transactionService.createTransaction(transaction, "USD")).thenReturn(transaction);

        // Act
        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction, "USD");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).createTransaction(transaction, "USD");
    }

    // ✅ Test for getUserTransactions
    @Test
    public void testGetUserTransactions() {
        // Arrange
        Page<Transaction> transactionPage = new PageImpl<>(Collections.singletonList(transaction));
        when(transactionService.getByUserIdAndFilters("testuser", "Food", "EXPENSE", null, null, Pageable.unpaged()))
                .thenReturn(transactionPage);

        // Act
        ResponseEntity<Page<Transaction>> response = transactionController.getUserTransactions(
                "testuser", "Food", "EXPENSE", null, null, Pageable.unpaged());

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(transactionPage, response.getBody());
        verify(transactionService, times(1))
                .getByUserIdAndFilters("testuser", "Food", "EXPENSE", null, null, Pageable.unpaged());
    }

    // ✅ Test for getTransactionById
    @Test
    public void testGetTransactionById() {
        // Arrange
        when(transactionService.getTransactionById("1")).thenReturn(Optional.of(transaction));

        // Act
        ResponseEntity<Optional<Transaction>> response = transactionController.getTransactionById("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Optional.of(transaction), response.getBody());
        verify(transactionService, times(1)).getTransactionById("1");
    }

    // ✅ Test for updateTransaction
    @Test
    public void testUpdateTransaction() {
        // Arrange
        when(transactionService.updateTransaction("1", transaction)).thenReturn(transaction);

        // Act
        ResponseEntity<Transaction> response = transactionController.updateTransaction("1", transaction);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).updateTransaction("1", transaction);
    }

    // ✅ Test for getExpenseTrends
    @Test
    public void testGetExpenseTrends() {
        // Arrange
        List<Map<String, Object>> expenseTrends = Collections.singletonList(Collections.singletonMap("month", "2023-10"));
        when(transactionService.getExpenseTrends("testuser")).thenReturn(expenseTrends);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = transactionController.getExpenseTrends("testuser");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expenseTrends, response.getBody());
        verify(transactionService, times(1)).getExpenseTrends("testuser");
    }

    // ✅ Test for getMonthlyExpenseReport
    @Test
    public void testGetMonthlyExpenseReport() {
        // Arrange
        List<Map<String, Object>> monthlyReport = Collections.singletonList(Collections.singletonMap("month", "2023-10"));
        when(transactionService.getMonthlyExpenseReport("testuser")).thenReturn(monthlyReport);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = transactionController.getMonthlyExpenseReport("testuser");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(monthlyReport, response.getBody());
        verify(transactionService, times(1)).getMonthlyExpenseReport("testuser");
    }

    // ✅ Test for deleteTransaction
    @Test
    public void testDeleteTransaction() {
        // Arrange
        doNothing().when(transactionService).deleteTransaction("1");

        // Act
        ResponseEntity<String> response = transactionController.deleteTransaction("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Transaction deleted successfully", response.getBody());
        verify(transactionService, times(1)).deleteTransaction("1");
    }

    // ✅ Test for getTransactionSummary
    @Test
    public void testGetTransactionSummary() {
        // Arrange
        Map<String, BigDecimal> summary = Collections.singletonMap("totalExpense", BigDecimal.valueOf(500));
        when(transactionService.getTransactionSummary("testuser")).thenReturn(summary);

        // Act
        ResponseEntity<Map<String, BigDecimal>> response = transactionController.getTransactionSummary("testuser");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(summary, response.getBody());
        verify(transactionService, times(1)).getTransactionSummary("testuser");
    }

    // ✅ Test for getIncomeVsExpenseTrends
    @Test
    public void testGetIncomeVsExpenseTrends() {
        // Arrange
        List<Map<String, Object>> trends = Collections.singletonList(Collections.singletonMap("month", "2023-10"));
        when(transactionService.getIncomeVsExpenseTrends("testuser")).thenReturn(trends);

        // Act
        ResponseEntity<List<Map<String, Object>>> response = transactionController.getIncomeVsExpenseTrends("testuser");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(trends, response.getBody());
        verify(transactionService, times(1)).getIncomeVsExpenseTrends("testuser");
    }

    // ✅ Test for getRecurringTransactions
    @Test
    public void testGetRecurringTransactions() {
        // Arrange
        List<Transaction> recurringTransactions = Collections.singletonList(transaction);
        when(transactionService.getRecurringTransactions("testuser")).thenReturn(recurringTransactions);

        // Act
        ResponseEntity<List<Transaction>> response = transactionController.getRecurringTransactions("testuser");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(recurringTransactions, response.getBody());
        verify(transactionService, times(1)).getRecurringTransactions("testuser");
    }

    // ✅ Test for stopRecurringTransaction
    @Test
    public void testStopRecurringTransaction() {
        // Arrange
        doNothing().when(transactionService).stopRecurringTransaction("1");

        // Act
        ResponseEntity<String> response = transactionController.stopRecurringTransaction("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Recurring transaction stopped.", response.getBody());
        verify(transactionService, times(1)).stopRecurringTransaction("1");
    }
}