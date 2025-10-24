package com.silverpalm.banking.core.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.silverpalm.banking.core.exception.BankingException;

/**
 * Abstract base class for all account types
 * Implements common functionality and defines abstract methods for specific account behaviors
 */
public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer customer;
    protected LocalDate dateOpened;
    protected AccountStatus status;
    protected AccountType accountType;
    protected List<Transaction> transactions;

    public Account(String accountNumber, double balance, String branch, 
                  Customer customer, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.customer = customer;
        this.dateOpened = LocalDate.now();
        this.status = AccountStatus.ACTIVE;
        this.accountType = accountType;
        this.transactions = new ArrayList<>();
    }

    // Abstract methods to be implemented by specific account types
    public abstract boolean canWithdraw();
    public abstract void withdraw(double amount) throws BankingException;

    // Common business logic for all accounts
    public void deposit(double amount) throws BankingException {
        if (amount <= 0) {
            throw new BankingException("Deposit amount must be positive");
        }
        if (status != AccountStatus.ACTIVE) {
            throw new BankingException("Cannot deposit to inactive account");
        }
        
        double oldBalance = balance;
        balance += amount;
        
        // Record transaction
        Transaction transaction = new Transaction(
            generateTransactionId(), 
            accountNumber, 
            TransactionType.DEPOSIT, 
            amount, 
            "Deposit to account",
            balance
        );
        transactions.add(transaction);
    }

    public void calculateInterest() {
        if (balance > 0 && status == AccountStatus.ACTIVE && accountType.getInterestRate() > 0) {
            double interest = balance * accountType.getInterestRate();
            double oldBalance = balance;
            balance += interest;
            
            // Record interest transaction
            Transaction transaction = new Transaction(
                generateTransactionId(),
                accountNumber,
                TransactionType.INTEREST,
                interest,
                "Monthly interest payment",
                balance
            );
            transactions.add(transaction);
        }
    }

    public void closeAccount() {
        this.status = AccountStatus.CLOSED;
    }

    // Transaction management
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> getRecentTransactions(int count) {
        int startIndex = Math.max(0, transactions.size() - count);
        return transactions.subList(startIndex, transactions.size());
    }

    protected void recordWithdrawalTransaction(double amount) {
        Transaction transaction = new Transaction(
            generateTransactionId(),
            accountNumber,
            TransactionType.WITHDRAWAL,
            amount,
            "Withdrawal from account",
            balance
        );
        transactions.add(transaction);
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public Customer getCustomer() { return customer; }
    public LocalDate getDateOpened() { return dateOpened; }
    public AccountStatus getStatus() { return status; }
    public AccountType getAccountType() { return accountType; }

    @Override
    public String toString() {
        return String.format("%s [%s] - Balance: BWP%.2f - %s", 
            accountType, accountNumber, balance, status);
    }
}