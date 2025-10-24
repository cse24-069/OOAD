package com.silverpalm.banking.core.model;

/**
 * Enum representing different types of bank accounts
 * Each account type has unique prefix for account number generation
 */
public enum AccountType {
    SAVINGS("SAV", 0.00025),    // 0.025% monthly interest
    INVESTMENT("INV", 0.00075), // 0.075% monthly interest  
    CHEQUE("CHQ", 0.0);         // No interest

    private final String prefix;
    private final double interestRate;

    AccountType(String prefix, double interestRate) {
        this.prefix = prefix;
        this.interestRate = interestRate;
    }

    public String getPrefix() { return prefix; }
    public double getInterestRate() { return interestRate; }
}