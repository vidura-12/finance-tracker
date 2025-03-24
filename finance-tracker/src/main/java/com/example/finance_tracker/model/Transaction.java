package com.example.finance_tracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String userId;
    private String category;
    private BigDecimal amount;
    private String type; // "INCOME" or "EXPENSE"
    private LocalDateTime date;
    private String description;

    // Recurring Transaction Fields
    private boolean isRecurring;
    private String recurrencePattern; // DAILY, WEEKLY, MONTHLY
    private LocalDateTime endDate; // Optional: When the recurrence stops

    // Multi-Currency Fields
    private String currency; // Currency of the transaction (e.g., USD, LKR, EUR)
    private BigDecimal convertedAmount; // Amount converted to base currency (e.g., USD)
    private BigDecimal exchangeRate; // Exchange rate used for conversion

    public Transaction() {}

    public Transaction(BigDecimal amount, String category, String currency, LocalDateTime date, LocalDateTime endDate, String id, String recurrencePattern, String userId, String type, boolean isRecurring, BigDecimal exchangeRate, String description, BigDecimal convertedAmount) {
        this.amount = amount;
        this.category = category;
        this.currency = currency;
        this.date = date;
        this.endDate = endDate;
        this.id = id;
        this.recurrencePattern = recurrencePattern;
        this.userId = userId;
        this.type = type;
        this.isRecurring = isRecurring;
        this.exchangeRate = exchangeRate;
        this.description = description;
        this.convertedAmount = convertedAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
