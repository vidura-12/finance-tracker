package com.example.controllers;

import com.example.finance_tracker.controllers.BudgetController;
import com.example.finance_tracker.model.Budget;
import com.example.finance_tracker.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private Budget budget;

    @BeforeEach
    public void setUp() {
        budget = new Budget();
        budget.setUserId("testuser");
        budget.setCategory("Food");
        budget.setLimitAmount(BigDecimal.valueOf(500));
        budget.setMonth(LocalDate.now().getMonthValue());
        budget.setYear(LocalDate.now().getYear());
    }

    // ✅ Test for setBudget
    @Test
    public void testSetBudget() {
        // Arrange
        when(budgetService.setBudget("testuser", "Food", BigDecimal.valueOf(500), 3, 2025))
                .thenReturn(budget);

        // Act
        ResponseEntity<Budget> response = budgetController.setBudget("testuser", "Food", BigDecimal.valueOf(500));

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(budget, response.getBody());
        verify(budgetService, times(1)).setBudget("testuser", "Food", BigDecimal.valueOf(500), 3, 2025);
    }

    // ✅ Test for getUserBudgets
    @Test
    public void testGetUserBudgets() {
        // Arrange
        List<Budget> budgets = Arrays.asList(budget);
        when(budgetService.getUserBudgets("testuser")).thenReturn(budgets);

        // Act
        ResponseEntity<List<Budget>> response = budgetController.getUserBudgets("testuser");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(budgets, response.getBody());
        verify(budgetService, times(1)).getUserBudgets("testuser");
    }

    // ✅ Test for checkBudgetStatus
    @Test
    public void testCheckBudgetStatus() {
        // Arrange
        when(budgetService.checkBudgetStatus("testuser", "Food", BigDecimal.valueOf(400), "test@example.com"))
                .thenReturn("Budget status: 80% spent");

        // Act
        ResponseEntity<String> response = budgetController.checkBudgetStatus("testuser", "Food", BigDecimal.valueOf(400), "test@example.com");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Budget status: 80% spent", response.getBody());
        verify(budgetService, times(1)).checkBudgetStatus("testuser", "Food", BigDecimal.valueOf(400), "test@example.com");
    }

    // ✅ Test for recommendBudgetAdjustments
    @Test
    public void testRecommendBudgetAdjustments() {
        // Arrange
        when(budgetService.recommendBudgetAdjustments("testuser", "Food"))
                .thenReturn("Recommended adjustment: Increase budget by 20%");

        // Act
        ResponseEntity<String> response = budgetController.recommendBudgetAdjustments("testuser", "Food");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Recommended adjustment: Increase budget by 20%", response.getBody());
        verify(budgetService, times(1)).recommendBudgetAdjustments("testuser", "Food");
    }

    // ✅ Test for suggestBudgetAdjustment
    @Test
    public void testSuggestBudgetAdjustment() {
        // Arrange
        when(budgetService.suggestBudgetAdjustment("testuser", "Food"))
                .thenReturn("Suggested adjustment: Reduce budget by 10%");

        // Act
        ResponseEntity<String> response = budgetController.suggestBudgetAdjustment("testuser", "Food");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Suggested adjustment: Reduce budget by 10%", response.getBody());
        verify(budgetService, times(1)).suggestBudgetAdjustment("testuser", "Food");
    }
}