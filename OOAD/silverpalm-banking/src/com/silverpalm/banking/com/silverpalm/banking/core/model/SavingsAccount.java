package com.silverpalm.banking.core.model;

import com.silverpalm.banking.core.exception.BankingException;

/**
 * Savings Account implementation
 * - Pays 0.025% monthly interest
 * - Does not allow withdrawals
 * - No minimum balance requirement
 */
public class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer, AccountType.SAVINGS);
    }

    @Override
    public boolean canWithdraw() {
        return false; // Savings accounts do not allow withdrawals
    }

    @Override
    public void withdraw(double amount) throws BankingException {
        throw new BankingException("Withdrawals are not allowed from Savings accounts");
    }

    @Override
    public void deposit(double amount) throws BankingException {
        super.deposit(amount);
        System.out.println("Funds securely deposited to Savings Account. Interest rate: 0.025% monthly");
    }
}