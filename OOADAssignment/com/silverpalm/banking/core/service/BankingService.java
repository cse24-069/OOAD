package com.silverpalm.banking.core.service;

import com.silverpalm.banking.core.model.*;
import com.silverpalm.banking.core.exception.BankingException;
import com.silverpalm.banking.dao.*;

import java.time.LocalDate;
import java.util.*;

public class BankingService {

    private final String bankName = "Silver Palm Bank";        // already final
    /*  runtime caches  */
    private final Map<String, Customer> customers = new HashMap<>();
    private final Map<String, Account> accounts = new HashMap<>();
    private final BankTeller bankTeller;                       // initialized in constructor
    private int customerCounter = 1;
    private final Random random = new Random();

    /*  in-memory DAOs – interface types  */
    private final ICustomerDao customerDao = new ICustomerDao() {
        @Override
        public void save(Customer c) {

        }

        @Override
        public Customer find(String customerId) {
            return null;
        }

        @Override
        public List<Customer> findAll() {
            return List.of();
        }
    };
    private final IAccountDao accountDao = new InMemoryAccountDao();
    private final ITransactionDao txnDao = new InMemoryTransactionDao();

    /* ======  Singleton  ====== */
    private static class Holder {
        private static final BankingService INSTANCE = new BankingService();
    }

    public static BankingService getInstance() {
        return Holder.INSTANCE;
    }

    /* ======  Private constructor  ====== */
    private BankingService() {
        // directly initialize the final field here
        this.bankTeller = new BankTeller(
                "TELL001",
                BankTeller.DEFAULT_USERNAME,
                BankTeller.DEFAULT_PASSWORD,
                "Abednigo",   // name kept as-is
                "Titus",
                "Gaborone Main"
        );
        initializeSampleData();
    }

    /* ------------------------------------------------------------------ */
    /*  sample data – in-memory only                                      */
    /* ------------------------------------------------------------------ */
    private void initializeSampleData() {
        try {
            Customer ind = createIndividualCustomer(
                    "john_doe", "password123", "John", "Doe",
                    LocalDate.of(1985, 5, 15), Gender.MALE, "123456789",
                    "123 Main St, Gaborone", "71123456", "john.doe@email.com", "Gaborone Main");

            Customer comp = createCompanyCustomer(
                    "tech_corp", "password123", "Tech Solutions Ltd",
                    "COMP123456", "Jane Smith", "456 Business Ave, Gaborone",
                    "72123456", "info@techsolutions.co.bw", "Gaborone Main");

            openSavingsAccount(ind.getCustomerId(), 1_000.00, "Gaborone Main");
            openInvestmentAccount(ind.getCustomerId(), 600.00, "Gaborone Main");
            openChequeAccount(comp.getCustomerId(), 5_000.00, "Gaborone Main",
                    comp.getCompanyName(), comp.getAddress());

        } catch (BankingException e) {
            System.out.println("Error initializing sample data: " + e.getMessage());
        }
    }
    public void processMonthlyInterest() {
        System.out.println("\n=== PROCESSING MONTHLY INTEREST ===");
        int count = 0;
        for (Account a : accounts.values()) {
            if (a.getBalance() > 0 && a.getStatus() == AccountStatus.ACTIVE) {
                double old = a.getBalance();
                a.calculateInterest();
                double interest = a.getBalance() - old;
                if (interest > 0) {
                    count++;
                    saveAccount(a);
                    Transaction t = a.getTransactions().get(a.getTransactions().size() - 1);
                    saveTransaction(t);
                    System.out.printf("Interest BWP%.2f -> %s%n", interest, a.getAccountNumber());
                }
            }
        }
        System.out.println("Interest processed. Accounts updated: " + count);
    }
    /* ------------------------------------------------------------------ */
    /*  persistence helpers  (in-memory)                                  */
    /* ------------------------------------------------------------------ */
    private void saveCustomer(Customer c) {
        customerDao.save(c);
    }

    private void saveAccount(Account a) {
        accountDao.save(a);
    }

    private void saveTransaction(Transaction t) {
        txnDao.save(t);
    }

    /* ------------------------------------------------------------------ */
    /*  core business logic                                               */
    /* ------------------------------------------------------------------ */
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
        saveCustomer(customer);
        System.out.println("Individual customer created: " + customerId);
        return customer;
    }

    public Customer createCompanyCustomer(String username, String password, String companyName,
                                          String registrationNumber, String contactPerson, String address,
                                          String phoneNumber, String email, String branch) throws BankingException {
        if (getCustomerByUsername(username) != null) {
            throw new BankingException("Username already exists");
        }
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new BankingException("Company registration number required");
        }
        String customerId = "COMP" + String.format("%04d", customerCounter++);
        Customer customer = new Customer(customerId, username, password, companyName,
                registrationNumber, contactPerson, address, phoneNumber, email, branch);
        customers.put(customerId, customer);
        customers.put(username, customer);
        saveCustomer(customer);
        System.out.println("Company customer created: " + customerId);
        return customer;
    }

    public Account openSavingsAccount(String customerId, double initialDeposit, String branch) throws BankingException {
        Customer c = getCustomer(customerId);
        if (c == null) throw new BankingException("Customer not found");
        if (c.getCustomerType() != CustomerType.INDIVIDUAL)
            throw new BankingException("Savings accounts for individuals only");
        String acctNo = generateAccountNumber(AccountType.SAVINGS);
        SavingsAccount acc = new SavingsAccount(acctNo, initialDeposit, branch, c);
        accounts.put(acctNo, acc);
        c.addAccount(acc);
        saveAccount(acc);
        System.out.println("Savings account opened: " + acctNo);
        return acc;
    }

    public Account openInvestmentAccount(String customerId, double initialDeposit, String branch) throws BankingException {
        Customer c = getCustomer(customerId);
        if (c == null) throw new BankingException("Customer not found");
        String acctNo = generateAccountNumber(AccountType.INVESTMENT);
        InvestmentAccount acc = new InvestmentAccount(acctNo, initialDeposit, branch, c);
        accounts.put(acctNo, acc);
        c.addAccount(acc);
        saveAccount(acc);
        System.out.println("Investment account opened: " + acctNo);
        return acc;
    }

    public Account openChequeAccount(String customerId, double initialDeposit, String branch,
                                     String employerName, String employerAddress) throws BankingException {
        Customer c = getCustomer(customerId);
        if (c == null) throw new BankingException("Customer not found");
        String acctNo = generateAccountNumber(AccountType.CHEQUE);
        ChequeAccount acc = new ChequeAccount(acctNo, initialDeposit, branch, c, employerName, employerAddress);
        accounts.put(acctNo, acc);
        c.addAccount(acc);
        saveAccount(acc);
        System.out.println("Cheque account opened: " + acctNo);
        return acc;
    }

    public Account openCompanyChequeAccount(String customerId, double initialDeposit, String branch) throws BankingException {
        Customer c = getCustomer(customerId);
        if (c == null) throw new BankingException("Customer not found");
        if (c.getCustomerType() != CustomerType.COMPANY)
            throw new BankingException("Method for companies only");
        return openChequeAccount(customerId, initialDeposit, branch, c.getCompanyName(), c.getAddress());
    }

    public void deposit(String accountNumber, double amount) throws BankingException {
        Account a = accounts.get(accountNumber);
        if (a == null) throw new BankingException("Account not found");
        a.deposit(amount);
        saveAccount(a);
        Transaction t = a.getTransactions().get(a.getTransactions().size() - 1);
        saveTransaction(t);
    }

    public void withdraw(String accountNumber, double amount) throws BankingException {
        Account a = accounts.get(accountNumber);
        if (a == null) throw new BankingException("Account not found");
        a.withdraw(amount);
        saveAccount(a);
        Transaction t = a.getTransactions().get(a.getTransactions().size() - 1);
        saveTransaction(t);
    }

    public void processSalaryPayment(String accountNumber, double salary) throws BankingException {
        Account a = accounts.get(accountNumber);
        if (a == null) throw new BankingException("Account not found");
        if (!(a instanceof ChequeAccount)) throw new BankingException("Salary only to cheque accounts");
        ((ChequeAccount) a).processSalaryPayment(salary);
        saveAccount(a);
        Transaction t = a.getTransactions().get(a.getTransactions().size() - 1);
        saveTransaction(t);
    }

    public double getAccountBalance(String accountNumber) throws BankingException {
        Account a = accounts.get(accountNumber);
        if (a == null) throw new BankingException("Account not found");
        return a.getBalance();
    }

    public List<Account> getCustomerAccounts(String customerId) {
        Customer c = getCustomer(customerId);
        return c != null ? c.getAccounts() : new ArrayList<>();
    }

    public List<Transaction> getRecentAccountTransactions(String accountNumber, int count) {
        Account a = accounts.get(accountNumber);
        return a != null ? a.getRecentTransactions(count) : new ArrayList<>();
    }

    public Customer loginCustomer(String username, String password) {
        Customer c = getCustomerByUsername(username);
        return (c != null && c.authenticate(username, password)) ? c : null;
    }

    public BankTeller loginTeller(String username, String password) {
        return bankTeller.authenticate(username, password) ? bankTeller : null;
    }

    private String generateAccountNumber(AccountType type) {
        String prefix = type.getPrefix();
        String digits = String.format("%011d", Math.abs(random.nextLong()) % 100_000_000_000L);
        return prefix + digits;
    }

    public String getBankName() {
        return bankName;
    }

    public BankTeller getBankTeller() {
        return bankTeller;
    }

    public int getTotalCustomers() {
        return getAllCustomers().size();
    }

    public int getIndividualCustomerCount() {
        return getIndividualCustomers().size();
    }

    public int getCompanyCustomerCount() {
        return getCompanyCustomers().size();
    }

    public int getTotalAccounts() {
        return accounts.size();
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public Customer getCustomerByUsername(String username) {
        return customers.get(username);
    }

    public List<Customer> getAllCustomers() {
        List<Customer> all = new ArrayList<>();
        for (Customer c : customers.values())
            if (c.getCustomerId().startsWith("IND") || c.getCustomerId().startsWith("COMP")) all.add(c);
        return all;
    }

    public List<Customer> getIndividualCustomers() {
        List<Customer> list = new ArrayList<>();
        for (Customer c : customers.values())
            if (c.getCustomerType() == CustomerType.INDIVIDUAL) list.add(c);
        return list;
    }

    public List<Customer> getCompanyCustomers() {
        List<Customer> list = new ArrayList<>();
        for (Customer c : customers.values())
            if (c.getCustomerType() == CustomerType.COMPANY) list.add(c);
        return list;
    }

    /* ---------- GUI trigger – safe to call again if needed ---------- */
    public void initGuiData() {
        /* already loaded */
    }
}
