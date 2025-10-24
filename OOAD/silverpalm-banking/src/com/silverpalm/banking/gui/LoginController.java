package com.silverpalm.banking.gui.controller;

import com.silverpalm.banking.core.service.BankingService;
import com.silverpalm.banking.gui.BankingApp;
import com.silverpalm.banking.gui.view.LoginView;
import javafx.scene.control.Alert;

public class LoginController {

    private final LoginView view;
    private final BankingService service = BankingService.getInstance();

    public LoginController(LoginView view) {
        this.view = view;
        view.getLoginButton().setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String user = view.getUsername();
        String pass = view.getPassword();
        boolean isCustomer = "Customer".equals(view.getRole());

        if (user.isEmpty() || pass.isEmpty()) {
            alert(Alert.AlertType.ERROR, "Fields cannot be empty.");
            return;
        }

        if (isCustomer) {
            var cust = service.loginCustomer(user, pass);
            if (cust == null) {
                alert(Alert.AlertType.ERROR, "Invalid customer credentials.");
            } else {
                BankingApp.showCustomerDash(cust.getCustomerId());
            }
        } else {
            var teller = service.loginTeller(user, pass);
            if (teller == null) {
                alert(Alert.AlertType.ERROR, "Invalid teller credentials.");
            } else {
                BankingApp.showTellerDash();
            }
        }
    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg).showAndWait();
    }
}