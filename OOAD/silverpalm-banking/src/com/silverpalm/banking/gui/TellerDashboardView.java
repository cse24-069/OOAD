package com.silverpalm.banking.gui.view;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TellerDashboardView extends BorderPane {

    private final ListView<String> lstCustomers = new ListView<>();
    private final Button btnRegInd   = new Button("Register Individual");
    private final Button btnRegComp  = new Button("Register Company");
    private final Button btnOpenAcct = new Button("Open Account");
    private final Button btnDep      = new Button("Deposit for Customer");
    private final Button btnWith     = new Button("Withdraw for Customer");
    private final Button btnInt      = new Button("Process Monthly Interest");
    private final Button btnLogout   = new Button("Logout");

    public TellerDashboardView() {
        setLeft(new VBox(10, new Label("Customers"), lstCustomers));
        VBox c = new VBox(15, btnRegInd, btnRegComp, btnOpenAcct, btnDep, btnWith, btnInt, btnLogout);
        setCenter(c);
    }

    public Button getRegIndButton()   { return btnRegInd; }
    public Button getRegCompButton()  { return btnRegComp; }
    public Button getOpenAcctButton() { return btnOpenAcct; }
    public Button getDepButton()      { return btnDep; }
    public Button getWithButton()     { return btnWith; }
    public Button getIntButton()      { return btnInt; }
    public Button getLogoutButton()   { return btnLogout; }
}