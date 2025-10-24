package com.silverpalm.banking.core.service;

import com.silverpalm.banking.core.model.*;
import com.silverpalm.banking.core.exception.BankingException;
import java.time.LocalDate;
import java.util.*;

public class BankingService {
    private String bankName;
    private Map<String, Customer> customers;
    private Map<String, Account> accounts;
    private BankTeller bankTeller;
    private int customerCounter;
    private Random random;

    public BankingService() {
        this.bankName = "Silver Palm Bank";
        this.customers = new HashMap<>();
        this.accounts = new HashMap<>();
        this.customerCounter = 1;
        this.random = new Random();
        initializeBankTeller();
        initializeSampleData();
    }

    private void initializeBankTeller() {
        this.bankTeller = new BankTeller(
            "TELL001",
            BankTeller.DEFAULT_USERNAME,
            BankTeller.DEFAULT_PASSWORD,
            "Abednigo",
            "Titus",
            "Gaborone Main"
        );
    }

    private void initializeSampleData() {
        try {
            // Create sample INDIVIDUAL customer
            Customer individualCustomer = createIndividualCustomer(
                "john_doe", "password123", "John", "Doe",
                LocalDate.of(1985, 5, 15), Gender.MALE, "123456789",
                "123 Main St, Gaborone", "71123456", "john.doe@email.com", "Gaborone Main"
            );

            // Create sample COMPANY customer
            Customer companyCustomer = createCompanyCustomer(
                "tech_corp", "password123", "Tech Solutions Ltd",
                "COMP123456", "Jane Smith", "456 Business Ave, Gaborone",
                "72123456", "info@techsolutions.co.bw", "Gaborone Main"
            );

            // Create accounts for individual
            openSavingsAccount(individualCustomer.getCustomerId(), 1000.00, "Gaborone Main");
            openInvestmentAccount(individualCustomer.getCustomerId(), 600.00, "Gaborone Main");

            // Create accounts for company
            openChequeAccount(companyCustomer.getCustomerId(), 5000.00, "Gaborone Main", 
                            "Tech Solutions Ltd", "456 Business Ave, Gaborone");

        } catch (BankingException e) {
            System.out.println("Error initializing sample data: " + e.getMessage());
        }
    }

    // INDIVIDUAL Customer Registration
    public Customer createIndividualCustomer(String username, String password, String firstName, String lastName,
                                           LocalDate dateOfBirth, Gender gender, String idNumber, String address,
                                           String phoneNumber, String email, String branch) throws BankingException {
        
        if (!idNumber.matches("\\d{9}")) {
            throw new BankingException("ID number must be exactly 9 digits");
        }

        if (getCustomerByUsername(username) != null) {
            throw new BankingException("Username already exists");
        }

        String customerId = "IND" + String.format("%04d", customerCounter++);
        Customer customer = new Customer(customerId, username, password, firstName, lastName,
                                       dateOfBirth, gender, idNumber, address, phoneNumber, email, branch);
        
        customers.put(customerId, customer);
        customers.put(username, customer);
        
        System.out.println("Individual customer created successfully: " + customerId);
        return customer;
    }

    // COMPANY Customer Registration
    public Customer createCompanyCustomer(String username, String password, String companyName,
                                        String registrationNumber, String contactPerson, String address,
                                        String phoneNumber, String email, String branch) throws BankingException {
        
        if (getCustomerByUsername(username) != null) {
            throw new BankingException("Username already exists");
        }

        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new BankingException("Company registration number is required");
        }

        String customerId = "COMP" + String.format("%04d", customerCounter++);
        Customer customer = new Customer(customerId, username, password, companyName,
                                       registrationNumber, contactPerson, address, phoneNumber, email, branch);
        
        customers.put(customerId, customer);
        customers.put(username, customer);
        
        System.out.println("Company customer created successfully: " + customerId);
        return customer;
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public Customer getCustomerByUsername(String username) {
        return customers.get(username);
    }

    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        for (Customer customer : customers.values()) {
            if (customer.getCustomerId().startsWith("IND") || customer.getCustomerId().startsWith("COMP")) {
                allCustomers.add(customer);
            }
        }
        return allCustomers;
    }

    public List<Customer> getIndividualCustomers() {
        List<Customer> individuals = new ArrayList<>();
        for (Customer customer : customers.values()) {
            if (customer.getCustomerType() == CustomerType.INDIVIDUAL) {
                individuals.add(customer);
            }
        }
        return individuals;
    }

    public List<Customer> getCompanyCustomers() {
        List<Customer> companies = new ArrayList<>();
        for (Customer customer : customers.values()) {
            if (customer.getCustomerType() == CustomerType.COMPANY) {
                companies.add(customer);
            }
        }
        return companies;
    }

    // Account Management with type restrictions
    public Account openSavingsAccount(String customerId, double initialDeposit, String branch) throws BankingException {
        Customer customer = getCustomer(customerId);
        if (customer == null) {
            throw new BankingException("Customer not found");
        }

        // Only individuals can open savings accounts
        if (customer.getCustomerType() != CustomerType.INDIVIDUAL) {
            throw new BankingException("Savings accounts are only available for individual customers");
        }

        String accountNumber = generateAccountNumber(AccountType.SAVINGS);
        SavingsAccount account = new SavingsAccount(accountNumber, initialDeposit, branch, customer);
        
        accounts.put(accountNumber, account);
        customer.addAccount(account);
        
        System.out.println("Savings account opened for individual: " + accountNumber);
        return account;
    }

    public Account openInvestmentAccount(String customerId, double initialDeposit, String branch) throws BankingException {
        Customer customer = getCustomer(customerId);
        if (customer == null) {
            throw new BankingException("Customer not found");
        }

        String accountNumber = generateAccountNumber(AccountType.INVESTMENT);
        InvestmentAccount account = new InvestmentAccount(accountNumber, initialDeposit, branch, customer);
        
        accounts.put(accountNumber, account);
        customer.addAccount(account);
        
        String customerType = customer.getCustomerType() == CustomerType.INDIVIDUAL ? "individual" : "company";
        System.out.println("Investment account opened for " + customerType + ": " + accountNumber);
        return account;
    }

    public Account openChequeAccount(String customerId, double initialDeposit, String branch,
                                   String employerName, String employerAddress) throws BankingException {
        Customer customer = getCustomer(customerId);
        if (customer == null) {
            throw new BankingException("Customer not found");
        }

        String accountNumber = generateAccountNumber(AccountType.CHEQUE);
        ChequeAccount account = new ChequeAccount(accountNumber, initialDeposit, branch, customer,
                                                employerName, employerAddress);
        
        accounts.put(accountNumber, account);
        customer.addAccount(account);
        
        String customerType = customer.getCustomerType() == CustomerType.INDIVIDUAL ? "individual" : "company";
        System.out.println("Cheque account opened for " + customerType + ": " + accountNumber);
        return account;
    }

    // Enhanced: Open cheque account for companies (simplified)
    public Account openCompanyChequeAccount(String customerId, double initialDeposit, String branch) throws BankingException {
        Customer customer = getCustomer(customerId);
        if (customer == null) {
            throw new BankingException("Customer not found");
        }

        if (customer.getCustomerType() != CustomerType.COMPANY) {
            throw new BankingException("This method is only for company customers");
        }

        // For companies, use their own details as employer info
        return openChequeAccount(customerId, initialDeposit, branch, 
                               customer.getCompanyName(), customer.getAddress());
    }

    // Rest of the methods remain the same but with customer type awareness
    public void deposit(String accountNumber, double amount) throws BankingException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new BankingException("Account not found");
        }
        account.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount) throws BankingException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new BankingException("Account not found");
        }
        account.withdraw(amount);
    }

    public void processSalaryPayment(String accountNumber, double salary) throws BankingException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new BankingException("Account not found");
        }
        if (!(account instanceof ChequeAccount)) {
            throw new BankingException("Salary payments can only be made to cheque accounts");
        }
        
        ChequeAccount chequeAccount = (ChequeAccount) account;
        chequeAccount.processSalaryPayment(salary);
    }

    public void processMonthlyInterest() {
        System.out.println("\n=== PROCESSING MONTHLY INTEREST ===");
        int count = 0;
        
        for (Account account : accounts.values()) {
            if (account.getBalance() > 0 && account.getStatus() == AccountStatus.ACTIVE) {
                double oldBalance = account.getBalance();
                account.calculateInterest();
                double interest = account.getBalance() - oldBalance;
                
                if (interest > 0) {
                    count++;
                    Customer customer = account.getCustomer();
                    String customerType = customer.getCustomerType() == CustomerType.INDIVIDUAL ? "Individual" : "Company";
                    System.out.printf("Interest of BWP%.2f added to %s account (%s)%n", 
                                    interest, account.getAccountNumber(), customerType);
                }
            }
        }
        
        System.out.println("Interest processing completed. " + count + " accounts updated.");
    }

    public double getAccountBalance(String accountNumber) throws BankingException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new BankingException("Account not found");
        }
        return account.getBalance();
    }

    public List<Account> getCustomerAccounts(String customerId) {
        Customer customer = getCustomer(customerId);
        return customer != null ? customer.getAccounts() : new ArrayList<>();
    }

    public List<Transaction> getAccountTransactions(String accountNumber) {
        Account account = accounts.get(accountNumber);
        return account != null ? account.getTransactions() : new ArrayList<>();
    }

    public List<Transaction> getRecentAccountTransactions(String accountNumber, int count) {
        Account account = accounts.get(accountNumber);
        return account != null ? account.getRecentTransactions(count) : new ArrayList<>();
    }

    public Customer loginCustomer(String username, String password) {
        Customer customer = getCustomerByUsername(username);
        if (customer != null && customer.authenticate(username, password)) {
            return customer;
        }
        return null;
    }

    public BankTeller loginTeller(String username, String password) {
        if (bankTeller.authenticate(username, password)) {
            return bankTeller;
        }
        return null;
    }

    private String generateAccountNumber(AccountType accountType) {
        String prefix = accountType.getPrefix();
        String randomDigits = String.format("%011d", Math.abs(random.nextLong()) % 100000000000L);
        return prefix + randomDigits;
    }

    // Enhanced statistics
    public String getBankName() { return bankName; }
    public int getTotalCustomers() { 
        int count = 0;
        for (String key : customers.keySet()) {
            if (key.startsWith("IND") || key.startsWith("COMP")) count++;
        }
        return count;
    }
    public int getIndividualCustomerCount() {
        return getIndividualCustomers().size();
    }
    public int getCompanyCustomerCount() {
        return getCompanyCustomers().size();
    }
    public int getTotalAccounts() { return accounts.size(); }
    public BankTeller getBankTeller() { return bankTeller; }
}

public static BankingService getInstance() {
    return Holder.INSTANCE;
}
private static class Holder {
    private static final BankingService INSTANCE = new BankingService();
}