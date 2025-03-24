package com.example.finance_tracker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Document(collection = "financial_goals")

public class FinancialGoal {
    @Id
    private String id;
    private String userId;
    private String goalName;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;
    private boolean isCompleted;
    private String category; // Example: "Car", "Vacation", etc.
    private boolean paused;

    public FinancialGoal(String category, String goalName, String id, boolean isCompleted, BigDecimal savedAmount, BigDecimal targetAmount, String userId, boolean paused) {
        this.category = category;
        this.goalName = goalName;
        this.id = id;
        this.isCompleted = isCompleted;
        this.savedAmount = savedAmount;
        this.targetAmount = targetAmount;
        this.userId = userId;
        this.paused = paused;
    }
    public FinancialGoal(){}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public BigDecimal getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(BigDecimal savedAmount) {
        this.savedAmount = savedAmount;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public BigDecimal getProgressPercentage() {
        return savedAmount.divide(targetAmount, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
