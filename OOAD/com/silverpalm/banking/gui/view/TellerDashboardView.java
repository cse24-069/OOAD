package com.silverpalm.banking.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TellerDashboardView extends BorderPane {
    private final String tellerId;

    /* ---------- controls ---------- */
    private final Label  lblWelcome   = new Label();
    private final ListView<String> lstCustomers = new ListView<>();

    private final Button btnRegInd    = new Button("Register Individual");
    private final Button btnRegComp   = new Button("Register Company");
    private final Button btnOpenAcct  = new Button("Open Account");
    private final Button btnDeposit   = new Button("Deposit for Customer");
    private final Button btnWithdraw  = new Button("Withdraw for Customer");
    private final Button btnHistory   = new Button("Transaction History");
    private final Button btnProfile   = new Button("Customer Profile");
    private final Button btnInt       = new Button("Process Monthly Interest");
    private final Button btnLogout    = new Button("Logout");

    public TellerDashboardView(String tellerId) {
        this.tellerId = tellerId;
        setPadding(new Insets(15));

        VBox left = new VBox(10, new Label("Select Customer"), lstCustomers);
        left.setPrefWidth(300);

        VBox center = new VBox(15,
                lblWelcome,
                btnRegInd, btnRegComp, btnOpenAcct,
                btnDeposit, btnWithdraw, btnHistory, btnProfile,
                btnInt, btnLogout);
        center.setAlignment(Pos.CENTER);

        setLeft(left);
        setCenter(center);
    }

    /* ---------- getters ---------- */
    public String getSelectedCustomer() { return lstCustomers.getSelectionModel().getSelectedItem(); }
    public String getTellerId() { return tellerId; }
    public void setWelcome(String name) { lblWelcome.setText("Welcome, " + name); }
    public void setCustomerList(java.util.List<String> customers) { lstCustomers.getItems().setAll(customers); }

    public Button getRegIndButton()    { return btnRegInd; }
    public Button getRegCompButton()   { return btnRegComp; }
    public Button getOpenAcctButton()  { return btnOpenAcct; }
    public Button getDepositButton()   { return btnDeposit; }
    public Button getWithdrawButton()  { return btnWithdraw; }
    public Button getHistoryButton()   { return btnHistory; }
    public Button getProfileButton()   { return btnProfile; }
    public Button getIntButton()       { return btnInt; }
    public Button getLogoutButton()    { return btnLogout; }
}