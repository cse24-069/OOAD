package com.silverpalm.banking.gui.controller;

import com.silverpalm.banking.core.service.BankingService;
import com.silverpalm.banking.dao.CustomerDao;
import com.silverpalm.banking.gui.BankingApp;
import com.silverpalm.banking.gui.view.TellerDashboardView;
import javafx.scene.control.Alert;

public class TellerDashboardController {

    private final TellerDashboardView view;
    private final BankingService service = BankingService.getInstance();

    public TellerDashboardController(TellerDashboardView view) {
        this.view = view;
        loadCustomers();
        wireEvents();
    }

    private void loadCustomers() {
        var dao = new CustomerDao();
        service.getAllCustomers().forEach(dao::save);
        view.setCustomerList(
                dao.findAll()
                        .stream()
                        .map(c -> c.getCustomerId() + " – " + c.getDisplayName())
                        .collect(java.util.stream.Collectors.toList())
        );
        view.setWelcome(service.getBankTeller().getFullName());
    }

    private void wireEvents() {
        view.getLogoutButton().setOnAction(e -> BankingApp.logout());
        view.getIntButton().setOnAction(e -> {
            service.processMonthlyInterest();  //  <-  FIXED SPELLING
            new Alert(Alert.AlertType.INFORMATION, "Monthly interest processed.").showAndWait();
        });

        view.getRegIndButton().setOnAction(e -> registerIndividual());
        view.getRegCompButton().setOnAction(e -> registerCompany());
        view.getOpenAcctButton().setOnAction(e -> openAccount());

        view.getDepositButton().setOnAction(e -> {
            String cust = view.getSelectedCustomer();
            if (cust == null) {
                new Alert(Alert.AlertType.WARNING, "No customer selected").showAndWait();
                return;
            }
            BankingApp.showDepositDialog(cust.split(" – ")[0]);
        });

        view.getWithdrawButton().setOnAction(e -> {
            String cust = view.getSelectedCustomer();
            if (cust == null) {
                new Alert(Alert.AlertType.WARNING, "No customer selected").showAndWait();
                return;
            }
            BankingApp.showWithdrawDialog(cust.split(" – ")[0]);
        });

        view.getHistoryButton().setOnAction(e -> {
            String cust = view.getSelectedCustomer();
            if (cust == null) {
                new Alert(Alert.AlertType.WARNING, "No customer selected").showAndWait();
                return;
            }
            BankingApp.showTransactionHistory(cust.split(" – ")[0]);
        });

        view.getProfileButton().setOnAction(e -> {
            String cust = view.getSelectedCustomer();
            if (cust == null) {
                new Alert(Alert.AlertType.WARNING, "No customer selected").showAndWait();
                return;
            }
            BankingApp.showCustomerProfile(cust.split(" – ")[0]);
        });
    }

    private void registerIndividual() {
        new com.silverpalm.banking.gui.view.RegisterIndividualDialog().showAndWait();
        loadCustomers();
    }

    private void registerCompany() {
        new com.silverpalm.banking.gui.view.RegisterCompanyDialog().showAndWait();
        loadCustomers();
    }

    private void openAccount() {
        String cust = view.getSelectedCustomer();
        if (cust == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a customer first").showAndWait();
            return;
        }
        String customerId = cust.split(" – ")[0];
        new com.silverpalm.banking.gui.view.OpenAccountDialog(customerId).showAndWait();
    }
}