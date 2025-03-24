package com.example.finance_tracker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "budgets")
public class Budget {
    @Id
    private String id;
    private String userId;
    private String category; // NULL means it's a general budget
    private BigDecimal limitAmount;
    private int month;
    private int year;

    public Budget(String category, int year, String userId, int month, BigDecimal limitAmount, String id) {
        this.category = category;
        this.year = year;
        this.userId = userId;
        this.month = month;
        this.limitAmount = limitAmount;
        this.id = id;
    }

    public Budget() {

    }

    public Budget(String userId, String category, BigDecimal limitAmount, int month, int year) {
        this.userId = userId;
        this.category = category;
        this.year = year;
        this.userId = userId;
        this.month = month;
        this.limitAmount = limitAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
