package com.silverpalm.banking.core.model;

import com.silverpalm.banking.core.exception.BankingException;

/**
 * Cheque Account implementation
 * - No interest payments
 * - Allows withdrawals with overdraft facility
 * - Requires employer information
 * - Used for salary payments
 */
public class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;
    private String employmentType;
    private double overdraftLimit;

    public ChequeAccount(String accountNumber, double balance, String branch, 
                        Customer customer, String employerName, String employerAddress) {
        super(accountNumber, balance, branch, customer, AccountType.CHEQUE);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
        this.employmentType = "Full-time";
        this.overdraftLimit = 1000.00; // Default overdraft limit
    }

    public ChequeAccount(String accountNumber, double balance, String branch, 
                        Customer customer, String employerName, String employerAddress, 
                        String employmentType, double overdraftLimit) {
        this(accountNumber, balance, branch, customer, employerName, employerAddress);
        this.employmentType = employmentType;
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public boolean canWithdraw() {
        return true;
    }

    @Override
    public void withdraw(double amount) throws BankingException {
        if (amount <= 0) {
            throw new BankingException("Withdrawal amount must be positive");
        }
        if (status != AccountStatus.ACTIVE) {
            throw new BankingException("Cannot withdraw from inactive account");
        }
        
        double availableBalance = balance + overdraftLimit;
        if (amount > availableBalance) {
            throw new BankingException(
                String.format("Insufficient funds. Available: BWP%.2f", availableBalance));
        }
        
        balance -= amount;
        recordWithdrawalTransaction(amount);
    }

    public void processSalaryPayment(double salary) throws BankingException {
        deposit(salary);
        System.out.println("Salary payment of BWP" + salary + " processed successfully");
    }

    // Getters
    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }
    public String getEmploymentType() { return employmentType; }
    public double getOverdraftLimit() { return overdraftLimit; }

    @Override
    public void deposit(double amount) throws BankingException {
        super.deposit(amount);
        System.out.println("Deposit successful to Cheque Account");
    }
}