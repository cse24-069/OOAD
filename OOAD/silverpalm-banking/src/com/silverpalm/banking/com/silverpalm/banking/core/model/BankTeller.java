package com.silverpalm.banking.core.model;

/**
 * Represents a bank teller with authentication and administrative privileges
 * Can create customers and accounts, process transactions
 */
public class BankTeller {
    private String tellerId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String branch;

    // Predefined teller credentials
    public static final String DEFAULT_USERNAME = "Abednigo";
    public static final String DEFAULT_PASSWORD = "0987654321";

    public BankTeller(String tellerId, String username, String password, 
                     String firstName, String lastName, String branch) {
        this.tellerId = tellerId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.branch = branch;
    }

    // Authentication
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // Getters
    public String getTellerId() { return tellerId; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getBranch() { return branch; }
    public String getFullName() { return firstName + " " + lastName; }

    @Override
    public String toString() {
        return String.format("Bank Teller: %s %s (%s)", firstName, lastName, tellerId);
    }
}