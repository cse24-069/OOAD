package com.silverpalm.banking.gui.controller;

import com.silverpalm.banking.core.service.BankingService;
import com.silverpalm.banking.gui.BankingApp;
import com.silverpalm.banking.gui.view.CustomerDashboardView;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class CustomerDashboardController {

    private final CustomerDashboardView view;
    private final BankingService service = BankingService.getInstance();
    private final String customerId;

    public CustomerDashboardController(CustomerDashboardView view) {
        this.view = view;
        this.customerId = view.customerId;
        loadCustomer();
        hookEvents();
    }

    private void loadCustomer() {
        var cust = service.getCustomer(customerId);
        view.setWelcome(cust.getFullName());
        var accts = service.getCustomerAccounts(customerId);
        accts.forEach(a -> view.setAccountList(java.util.List.of(
                a.getAccountNumber() + "  [" + a.getAccountType() + "]  BWP " + String.format("%.2f", a.getBalance()))));
    }

    private void hookEvents() {
        view.getDepositButton().setOnAction(e -> handleDeposit());
        view.getWithdrawButton().setOnAction(e -> handleWithdraw());
        view.getHistoryButton().setOnAction(e -> showHistory());
        view.getProfileButton().setOnAction(e -> showProfile());
        view.getLogoutButton().setOnAction(e -> BankingApp.logout());
    }

    private void handleDeposit() {
        String acct = extractAccountNumber();
        if (acct == null) return;
        Optional<String> res = dialog("Deposit amount:", "Deposit");
        res.ifPresent(s -> {
            try {
                double amt = Double.parseDouble(s);
                service.deposit(acct, amt);
                alert("Deposit successful.");
                loadCustomer();
            } catch (Exception ex) { alert(ex.getMessage()); }
        });
    }

    private void handleWithdraw() {
        String acct = extractAccountNumber();
        if (acct == null) return;
        Optional<String> res = dialog("Withdrawal amount:", "Withdraw");
        res.ifPresent(s -> {
            try {
                double amt = Double.parseDouble(s);
                service.withdraw(acct, amt);
                alert("Withdrawal successful.");
                loadCustomer();
            } catch (Exception ex) { alert(ex.getMessage()); }
        });
    }

    private void showHistory() {
        String acct = extractAccountNumber();
        if (acct == null) return;
        var txs = service.getRecentAccountTransactions(acct, 20);
        StringBuilder sb = new StringBuilder();
        txs.forEach(t -> sb.append(t).append('\n'));
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setHeaderText("Last 20 transactions");
        al.setContentText(sb.toString());
        al.showAndWait();
    }

    private void showProfile() {
        var cust = service.getCustomer(customerId);
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.setHeaderText("Customer Profile");
        al.setContentText(cust.toString());
        al.showAndWait();
    }

    private String extractAccountNumber() {
        String sel = view.getSelectedAccount();
        if (sel == null) { alert("Please select an account."); return null; }
        return sel.split("\\s+")[0];
    }

    private Optional<String> dialog(String prompt, String title) {
        TextInputDialog d = new TextInputDialog();
        d.setTitle(title);
        d.setHeaderText(prompt);
        return d.showAndWait();
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}