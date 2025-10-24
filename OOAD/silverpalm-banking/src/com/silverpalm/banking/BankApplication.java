import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import com.silverpalm.banking.core.exception.BankingException;
import com.silverpalm.banking.core.model.Account;
import com.silverpalm.banking.core.model.BankTeller;
import com.silverpalm.banking.core.model.Customer;
import com.silverpalm.banking.core.model.CustomerType;
import com.silverpalm.banking.core.model.Gender;
import com.silverpalm.banking.core.model.Transaction;
import com.silverpalm.banking.core.service.BankingService;

public class BankApplication {
    private static BankingService bankingService = new BankingService();
    private static Scanner scanner = new Scanner(System.in);
    private static Customer currentCustomer = null;
    private static BankTeller currentTeller = null;

    public static void main(String[] args) {
        displayWelcomeMessage();
        
        boolean running = true;
        while (running) {
            if (currentCustomer == null && currentTeller == null) {
                running = displayMainMenu();
            } else if (currentCustomer != null) {
                displayCustomerMenu();
            } else if (currentTeller != null) {
                displayTellerMenu();
            }
        }
        
        scanner.close();
        System.out.println("Thank you for using " + bankingService.getBankName() + "!");
    }

    private static void displayWelcomeMessage() {
        System.out.println("===============================================");
        System.out.println("       WELCOME TO SILVER PALM BANK");
        System.out.println("===============================================");
        System.out.println("Your Trusted Banking Partner Since 2024");
        System.out.println();
    }

    private static boolean displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Customer Login");
        System.out.println("2. Bank Teller Login");
        System.out.println("3. Exit System");
        System.out.print("Please choose an option: ");

        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                handleCustomerLogin();
                break;
            case 2:
                handleTellerLogin();
                break;
            case 3:
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }

    private static void handleCustomerLogin() {
        System.out.println("\n=== CUSTOMER LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentCustomer = bankingService.loginCustomer(username, password);
        if (currentCustomer != null) {
            String customerType = currentCustomer.getCustomerType() == CustomerType.INDIVIDUAL ? "Individual" : "Company";
            System.out.println("Login successful! Welcome, " + currentCustomer.getFullName() + " (" + customerType + ")!");
        } else {
            System.out.println("Login failed! Invalid username or password.");
        }
    }

    private static void handleTellerLogin() {
        System.out.println("\n=== BANK TELLER LOGIN ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentTeller = bankingService.loginTeller(username, password);
        if (currentTeller != null) {
            System.out.println("Teller login successful! Welcome, " + currentTeller.getFirstName() + "!");
        } else {
            System.out.println("Login failed! Invalid credentials.");
        }
    }

    private static void displayCustomerMenu() {
        System.out.println("\n=== CUSTOMER DASHBOARD ===");
        System.out.println("1. View My Accounts");
        System.out.println("2. View Account Balance");
        System.out.println("3. Make Deposit");
        System.out.println("4. Make Withdrawal");
        System.out.println("5. View Account Transactions");
        System.out.println("6. View My Profile");
        System.out.println("7. Logout");
        System.out.print("Choose an option: ");

        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                viewCustomerAccounts();
                break;
            case 2:
                checkAccountBalance();
                break;
            case 3:
                makeDeposit();
                break;
            case 4:
                makeWithdrawal();
                break;
            case 5:
                viewAccountTransactions();
                break;
            case 6:
                viewCustomerProfile();
                break;
            case 7:
                System.out.println("Goodbye, " + currentCustomer.getFullName() + "!");
                currentCustomer = null;
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void displayTellerMenu() {
        System.out.println("\n=== BANK TELLER DASHBOARD ===");
        System.out.println("1. Register Individual Customer");
        System.out.println("2. Register Company Customer");
        System.out.println("3. Open New Account");
        System.out.println("4. View Customer Accounts");
        System.out.println("5. Make Deposit for Customer");
        System.out.println("6. Make Withdrawal for Customer");
        System.out.println("7. Process Salary Payment");
        System.out.println("8. Process Monthly Interest");
        System.out.println("9. View Bank Statistics");
        System.out.println("10. View All Customers");
        System.out.println("11. Logout");
        System.out.print("Choose an option: ");

        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                registerIndividualCustomer();
                break;
            case 2:
                registerCompanyCustomer();
                break;
            case 3:
                openNewAccount();
                break;
            case 4:
                viewCustomerAccountsForTeller();
                break;
            case 5:
                makeDepositForCustomer();
                break;
            case 6:
                makeWithdrawalForCustomer();
                break;
            case 7:
                processSalaryPayment();
                break;
            case 8:
                processMonthlyInterest();
                break;
            case 9:
                displayBankStatistics();
                break;
            case 10:
                viewAllCustomers();
                break;
            case 11:
                System.out.println("Teller session ended.");
                currentTeller = null;
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    // INDIVIDUAL Customer Registration
    private static void registerIndividualCustomer() {
        System.out.println("\n=== REGISTER INDIVIDUAL CUSTOMER ===");
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        LocalDate dateOfBirth = getDateInput();
        
        System.out.print("Gender (MALE/FEMALE/OTHER): ");
        Gender gender = getGenderInput();
        
        System.out.print("ID Number (9 digits): ");
        String idNumber = scanner.nextLine();
        
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Branch: ");
        String branch = scanner.nextLine();

        try {
            Customer customer = bankingService.createIndividualCustomer(
                username, password, firstName, lastName, dateOfBirth, gender,
                idNumber, address, phoneNumber, email, branch
            );
            System.out.println("Individual customer registered successfully! Customer ID: " + customer.getCustomerId());
        } catch (BankingException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    // COMPANY Customer Registration
    private static void registerCompanyCustomer() {
        System.out.println("\n=== REGISTER COMPANY CUSTOMER ===");
        
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Company Name: ");
        String companyName = scanner.nextLine();
        System.out.print("Registration Number: ");
        String regNumber = scanner.nextLine();
        System.out.print("Contact Person: ");
        String contactPerson = scanner.nextLine();
        System.out.print("Company Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Branch: ");
        String branch = scanner.nextLine();

        try {
            Customer customer = bankingService.createCompanyCustomer(
                username, password, companyName, regNumber, contactPerson,
                address, phoneNumber, email, branch
            );
            System.out.println("Company customer registered successfully! Customer ID: " + customer.getCustomerId());
        } catch (BankingException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void openNewAccount() {
        System.out.println("\n=== OPEN NEW ACCOUNT ===");
        System.out.print("Customer ID: ");
        String customerId = scanner.nextLine();
        
        Customer customer = bankingService.getCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.println("Account Types:");
        System.out.println("1. Savings Account (0.025% interest, no withdrawals) - INDIVIDUALS ONLY");
        System.out.println("2. Investment Account (0.075% interest, min BWP500 opening)");
        System.out.println("3. Cheque Account (no interest, overdraft facility)");
        System.out.print("Choose account type: ");
        
        int accountType = getIntInput();
        System.out.print("Initial deposit: ");
        double initialDeposit = getDoubleInput();
        System.out.print("Branch: ");
        String branch = scanner.nextLine();

        try {
            Account account = null;
            switch (accountType) {
                case 1:
                    account = bankingService.openSavingsAccount(customerId, initialDeposit, branch);
                    break;
                case 2:
                    account = bankingService.openInvestmentAccount(customerId, initialDeposit, branch);
                    break;
                case 3:
                    if (customer.getCustomerType() == CustomerType.INDIVIDUAL) {
                        System.out.print("Employer Name: ");
                        String employerName = scanner.nextLine();
                        System.out.print("Employer Address: ");
                        String employerAddress = scanner.nextLine();
                        account = bankingService.openChequeAccount(customerId, initialDeposit, branch, 
                                                                  employerName, employerAddress);
                    } else {
                        // For companies, use their own details
                        account = bankingService.openCompanyChequeAccount(customerId, initialDeposit, branch);
                    }
                    break;
                default:
                    System.out.println("Invalid account type!");
                    return;
            }
            System.out.println("Account opened successfully! Account Number: " + account.getAccountNumber());
        } catch (BankingException e) {
            System.out.println("Error opening account: " + e.getMessage());
        }
    }

    private static void viewCustomerAccountsForTeller() {
        System.out.print("Enter Customer ID: ");
        String customerId = scanner.nextLine();
        
        Customer customer = bankingService.getCustomer(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.println("\n=== ACCOUNTS FOR " + customer.getDisplayName().toUpperCase() + " ===");
        List<Account> accounts = bankingService.getCustomerAccounts(customerId);
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found for this customer.");
        } else {
            for (Account account : accounts) {
                System.out.println(account);
                List<Transaction> transactions = bankingService.getRecentAccountTransactions(
                    account.getAccountNumber(), 3);
                if (!transactions.isEmpty()) {
                    System.out.println("  Recent transactions:");
                    for (Transaction transaction : transactions) {
                        System.out.println("    " + transaction);
                    }
                }
                System.out.println();
            }
        }
    }

    private static void viewAllCustomers() {
        System.out.println("\n=== ALL CUSTOMERS ===");
        
        List<Customer> individuals = bankingService.getIndividualCustomers();
        List<Customer> companies = bankingService.getCompanyCustomers();
        
        System.out.println("\nINDIVIDUAL CUSTOMERS:");
        if (individuals.isEmpty()) {
            System.out.println("  No individual customers found.");
        } else {
            for (Customer customer : individuals) {
                System.out.println("  " + customer.getDisplayName() + " - " + customer.getCustomerId());
            }
        }
        
        System.out.println("\nCOMPANY CUSTOMERS:");
        if (companies.isEmpty()) {
            System.out.println("  No company customers found.");
        } else {
            for (Customer customer : companies) {
                System.out.println("  " + customer.getDisplayName() + " - " + customer.getCustomerId());
            }
        }
        
        System.out.println("\nTotal: " + individuals.size() + " individual(s), " + 
                         companies.size() + " company/companies");
    }

    private static void viewCustomerProfile() {
        System.out.println("\n=== YOUR PROFILE ===");
        Customer customer = currentCustomer;
        
        if (customer.getCustomerType() == CustomerType.INDIVIDUAL) {
            System.out.println("Type: Individual Customer");
            System.out.println("Name: " + customer.getFirstName() + " " + customer.getLastName());
            System.out.println("Date of Birth: " + customer.getDateOfBirth());
            System.out.println("Gender: " + customer.getGender());
            System.out.println("ID Number: " + customer.getIdNumber());
        } else {
            System.out.println("Type: Company Customer");
            System.out.println("Company Name: " + customer.getCompanyName());
            System.out.println("Registration Number: " + customer.getRegistrationNumber());
            System.out.println("Contact Person: " + customer.getContactPerson());
        }
        
        System.out.println("Address: " + customer.getAddress());
        System.out.println("Phone: " + customer.getPhoneNumber());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Branch: " + customer.getBranch());
        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Registration Date: " + customer.getRegistrationDate());
    }

    // Enhanced bank statistics
    private static void displayBankStatistics() {
        System.out.println("\n=== BANK STATISTICS ===");
        System.out.println("Bank Name: " + bankingService.getBankName());
        System.out.println("Total Customers: " + bankingService.getTotalCustomers());
        System.out.println("  - Individual Customers: " + bankingService.getIndividualCustomerCount());
        System.out.println("  - Company Customers: " + bankingService.getCompanyCustomerCount());
        System.out.println("Total Accounts: " + bankingService.getTotalAccounts());
        System.out.println("Active Teller: " + bankingService.getBankTeller().getFullName());
    }

    // Rest of the utility methods remain the same...
    private static void viewCustomerAccounts() {
        System.out.println("\n=== YOUR ACCOUNTS ===");
        List<Account> accounts = bankingService.getCustomerAccounts(currentCustomer.getCustomerId());
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            System.out.println("Account Number        Type           Balance          Status");
            System.out.println("----------------------------------------------------------------");
            for (Account account : accounts) {
                System.out.printf("%-20s %-14s BWP%-12.2f %-10s%n",
                    account.getAccountNumber(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.getStatus());
            }
        }
    }

    private static void checkAccountBalance() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        
        try {
            double balance = bankingService.getAccountBalance(accountNumber);
            System.out.printf("Current balance for account %s: BWP%.2f%n", accountNumber, balance);
        } catch (BankingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void makeDeposit() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter deposit amount: ");
        double amount = getDoubleInput();
        
        try {
            bankingService.deposit(accountNumber, amount);
            System.out.printf("Deposit of BWP%.2f successful!%n", amount);
        } catch (BankingException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    private static void makeWithdrawal() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter withdrawal amount: ");
        double amount = getDoubleInput();
        
        try {
            bankingService.withdraw(accountNumber, amount);
            System.out.printf("Withdrawal of BWP%.2f successful!%n", amount);
        } catch (BankingException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    private static void viewAccountTransactions() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        
        List<Transaction> transactions = bankingService.getRecentAccountTransactions(accountNumber, 10);
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
        } else {
            System.out.println("\n=== RECENT TRANSACTIONS ===");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    private static void makeDepositForCustomer() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter deposit amount: ");
        double amount = getDoubleInput();
        
        try {
            bankingService.deposit(accountNumber, amount);
            System.out.printf("Deposit of BWP%.2f processed successfully!%n", amount);
        } catch (BankingException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    private static void makeWithdrawalForCustomer() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter withdrawal amount: ");
        double amount = getDoubleInput();
        
        try {
            bankingService.withdraw(accountNumber, amount);
            System.out.printf("Withdrawal of BWP%.2f processed successfully!%n", amount);
        } catch (BankingException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }

    private static void processSalaryPayment() {
        System.out.print("Enter cheque account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter salary amount: ");
        double salary = getDoubleInput();
        
        try {
            bankingService.processSalaryPayment(accountNumber, salary);
            System.out.println("Salary payment processed successfully!");
        } catch (BankingException e) {
            System.out.println("Salary payment failed: " + e.getMessage());
        }
    }

    private static void processMonthlyInterest() {
        System.out.println("Processing monthly interest for all accounts...");
        bankingService.processMonthlyInterest();
        System.out.println("Monthly interest processing completed!");
    }

    // Utility methods
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    private static double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid amount.");
            scanner.next();
        }
        double input = scanner.nextDouble();
        scanner.nextLine();
        return input;
    }

    private static LocalDate getDateInput() {
        while (true) {
            try {
                String dateStr = scanner.nextLine();
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.print("Invalid date format. Please use YYYY-MM-DD: ");
            }
        }
    }

    private static Gender getGenderInput() {
        while (true) {
            try {
                String genderStr = scanner.nextLine().toUpperCase();
                return Gender.valueOf(genderStr);
            } catch (IllegalArgumentException e) {
                System.out.print("Invalid gender. Please enter MALE, FEMALE, or OTHER: ");
            }
        }
    }
}