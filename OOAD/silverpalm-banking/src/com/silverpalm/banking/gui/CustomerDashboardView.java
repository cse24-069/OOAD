package com.silverpalm.banking.gui.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CustomerDashboardView extends BorderPane {

    private final String customerId;
    private final ListView<String> lstAccounts = new ListView<>();
    private final Button btnDeposit   = new Button("Deposit");
    private final Button btnWithdraw  = new Button("Withdraw");
    private final Button btnHistory   = new Button("Transaction History");
    private final Button btnProfile   = new Button("My Profile");
    private final Button btnLogout    = new Button("Logout");
    private final Label  lblWelcome   = new Label();

    public CustomerDashboardView(String customerId) {
        this.customerId = customerId;
        setPadding(new Insets(15));

        VBox left = new VBox(10, new Label("Your Accounts"), lstAccounts);
        left.setPrefWidth(300);

        VBox center = new VBox(15, lblWelcome, btnDeposit, btnWithdraw, btnHistory, btnProfile, btnLogout);
        center.setAlignment(javafx.geometry.Pos.CENTER);

        setLeft(left);
        setCenter(center);
    }

    public String getSelectedAccount() { return lstAccounts.getSelectionModel().getSelectedItem(); }
    public void setWelcome(String name) { lblWelcome.setText("Welcome, " + name); }
    public void setAccountList(java.util.List<String> accts) { lstAccounts.getItems().setAll(accts); }

    public Button getDepositButton()   { return btnDeposit; }
    public Button getWithdrawButton()  { return btnWithdraw; }
    public Button getHistoryButton()   { return btnHistory; }
    public Button getProfileButton()   { return btnProfile; }
    public Button getLogoutButton()    { return btnLogout; }
}