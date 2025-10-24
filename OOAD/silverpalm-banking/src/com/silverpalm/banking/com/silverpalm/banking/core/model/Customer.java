package com.silverpalm.banking.core.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String username;
    private String password;
    private CustomerType customerType;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String idNumber;
    private String address;
    private String phoneNumber;
    private String email;
    private String branch;
    private LocalDate registrationDate;
    private List<Account> accounts;
    
    // Company-specific fields
    private String companyName;
    private String registrationNumber;
    private String contactPerson;

    // Constructor for INDIVIDUAL customers
    public Customer(String customerId, String username, String password, 
                   String firstName, String lastName, LocalDate dateOfBirth, 
                   Gender gender, String idNumber, String address, 
                   String phoneNumber, String email, String branch) {
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.customerType = CustomerType.INDIVIDUAL;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.idNumber = idNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.branch = branch;
        this.registrationDate = LocalDate.now();
        this.accounts = new ArrayList<>();
    }

    // Constructor for COMPANY customers
    public Customer(String customerId, String username, String password,
                   String companyName, String registrationNumber,
                   String contactPerson, String address, String phoneNumber, 
                   String email, String branch) {
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.customerType = CustomerType.COMPANY;
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.contactPerson = contactPerson;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.branch = branch;
        this.registrationDate = LocalDate.now();
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        if (!accounts.contains(account)) {
            accounts.add(account);
        }
    }

    public Account getAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // Getters
    public String getCustomerId() { return customerId; }
    public String getUsername() { return username; }
    public CustomerType getCustomerType() { return customerType; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Gender getGender() { return gender; }
    public String getIdNumber() { return idNumber; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getBranch() { return branch; }
    public String getCompanyName() { return companyName; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getContactPerson() { return contactPerson; }
    public LocalDate getRegistrationDate() { return registrationDate; } // ADDED THIS METHOD
    
    public String getFullName() { 
        if (customerType == CustomerType.INDIVIDUAL) {
            return firstName + " " + lastName;
        } else {
            return companyName;
        }
    }
    
    public String getDisplayName() {
        if (customerType == CustomerType.INDIVIDUAL) {
            return firstName + " " + lastName + " (Individual)";
        } else {
            return companyName + " (Company)";
        }
    }

    @Override
    public String toString() {
        if (customerType == CustomerType.INDIVIDUAL) {
            return String.format("Individual Customer: %s %s (ID: %s)", firstName, lastName, customerId);
        } else {
            return String.format("Company Customer: %s (ID: %s)", companyName, customerId);
        }
    }
}