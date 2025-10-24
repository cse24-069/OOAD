package com.silverpalm.banking.core.model;

import java.time.LocalDateTime;

/**
 * Represents a financial transaction on an account
 * Records all deposits, withdrawals, and interest payments
 */
public class Transaction {
    private String transactionId;
    private String accountNumber;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
    private String description;
    private double balanceAfter;

    public Transaction(String transactionId, String accountNumber, TransactionType type, 
                      double amount, String description, double balanceAfter) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.description = description;
        this.balanceAfter = balanceAfter;
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public double getBalanceAfter() { return balanceAfter; }

    @Override
    public String toString() {
        return String.format("%s | %s | BWP%.2f | Balance: BWP%.2f | %s", 
            timestamp, type, amount, balanceAfter, description);
    }
}