package com.silverpalm.banking.core.model;

import com.silverpalm.banking.core.exception.BankingException;

/**
 * Investment Account implementation
 * - Pays 0.075% monthly interest
 * - Allows withdrawals
 * - Requires minimum opening balance of BWP500
 * - Requires minimum balance of BWP100 after withdrawals
 */
public class InvestmentAccount extends Account {
    private static final double MIN_OPENING_BALANCE = 500.00;
    private static final double MIN_BALANCE = 100.00;

    public InvestmentAccount(String accountNumber, double balance, String branch, Customer customer) 
            throws BankingException {
        super(accountNumber, balance, branch, customer, AccountType.INVESTMENT);
        
        if (balance < MIN_OPENING_BALANCE) {
            throw new BankingException(
                String.format("Investment account requires minimum opening balance of BWP%.2f", 
                            MIN_OPENING_BALANCE));
        }
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
        if (amount > balance) {
            throw new BankingException("Insufficient funds for withdrawal");
        }
        
        double newBalance = balance - amount;
        if (newBalance < MIN_BALANCE) {
            throw new BankingException(
                String.format("Withdrawal would bring balance below minimum required BWP%.2f", 
                            MIN_BALANCE));
        }
        
        balance = newBalance;
        recordWithdrawalTransaction(amount);
    }

    @Override
    public void deposit(double amount) throws BankingException {
        super.deposit(amount);
        System.out.println("Deposit successful. Investment account interest rate: 0.075% monthly");
    }
}