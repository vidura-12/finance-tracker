package com.example.controllers;

import com.example.finance_tracker.controllers.FinancialGoalController;
import com.example.finance_tracker.model.FinancialGoal;
import com.example.finance_tracker.service.FinancialGoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinancialGoalControllerTest {

    @Mock
    private FinancialGoalService goalService;

    @InjectMocks
    private FinancialGoalController financialGoalController;

    private FinancialGoal financialGoal;

    @BeforeEach
    public void setUp() {
        financialGoal = new FinancialGoal();
        financialGoal.setCategory("Car");
        financialGoal.setId("1");
        financialGoal.setUserId("testuser");
        financialGoal.setGoalName("Save for Car");
        financialGoal.setTargetAmount(BigDecimal.valueOf(10000));

    }

    // ✅ Test for createGoal
    @Test
    public void testCreateGoal() {
        // Arrange
        when(goalService.createGoal(financialGoal)).thenReturn(financialGoal);

        // Act
        ResponseEntity<FinancialGoal> response = financialGoalController.createGoal(financialGoal);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(financialGoal, response.getBody());
        verify(goalService, times(1)).createGoal(financialGoal);
    }

    // ✅ Test for getUserGoals
    @Test
    public void testGetUserGoals() {
        // Arrange
        List<FinancialGoal> goals = Arrays.asList(financialGoal);
        when(goalService.getUserGoals("testuser")).thenReturn(goals);

        // Act
        ResponseEntity<List<FinancialGoal>> response = financialGoalController.getUserGoals("testuser");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(goals, response.getBody());
        verify(goalService, times(1)).getUserGoals("testuser");
    }

    // ✅ Test for updateGoalProgress
    @Test
    public void testUpdateGoalProgress() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(500);
        when(goalService.updateGoalProgress("1", amount)).thenReturn(financialGoal);

        // Act
        ResponseEntity<FinancialGoal> response = financialGoalController.updateGoalProgress("1", amount);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(financialGoal, response.getBody());
        verify(goalService, times(1)).updateGoalProgress("1", amount);
    }

    // ✅ Test for deleteGoal
    @Test
    public void testDeleteGoal() {
        // Arrange
        doNothing().when(goalService).deleteGoal("1");

        // Act
        ResponseEntity<String> response = financialGoalController.deleteGoal("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Goal deleted successfully", response.getBody());
        verify(goalService, times(1)).deleteGoal("1");
    }

    // ✅ Test for pauseGoal
    @Test
    public void testPauseGoal() {
        // Arrange
        when(goalService.pauseGoal("1")).thenReturn(financialGoal);

        // Act
        ResponseEntity<FinancialGoal> response = financialGoalController.pauseGoal("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(financialGoal, response.getBody());
        verify(goalService, times(1)).pauseGoal("1");
    }

    // ✅ Test for resumeGoal
    @Test
    public void testResumeGoal() {
        // Arrange
        when(goalService.resumeGoal("1")).thenReturn(financialGoal);

        // Act
        ResponseEntity<FinancialGoal> response = financialGoalController.resumeGoal("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(financialGoal, response.getBody());
        verify(goalService, times(1)).resumeGoal("1");
    }
}