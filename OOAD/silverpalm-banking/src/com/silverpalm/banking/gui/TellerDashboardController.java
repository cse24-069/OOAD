package com.silverpalm.banking.gui.controller;

import com.silverpalm.banking.core.service.BankingService;
import com.silverpalm.banking.gui.BankingApp;
import com.silverpalm.banking.gui.view.TellerDashboardView;
import javafx.scene.control.Alert;

public class TellerDashboardController {

    private final TellerDashboardView view;
    private final BankingService service = BankingService.getInstance();

    public TellerDashboardController(TellerDashboardView view) {
        this.view = view;
        view.getLogoutButton().setOnAction(e -> BankingApp.logout());
        // TODO wire remaining buttons – identical pattern to customer controller
        view.getIntButton().setOnAction(e -> {
            service.processMonthlyInterest();
            new Alert(Alert.AlertType.INFORMATION, "Monthly interest processed.").showAndWait();
        });
    }
}