package com.silverpalm.banking.gui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LoginView extends VBox {

    private final TextField txtUser = new TextField();
    private final PasswordField txtPass = new PasswordField();
    private final ComboBox<String> cboRole = new ComboBox<>();
    private final Button btnLogin = new Button("Login");

    public LoginView() {
        setSpacing(10);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(30));

        cboRole.getItems().addAll("Customer", "Bank Teller");
        cboRole.setValue("Customer");

        GridPane grid = new GridPane();
        grid.setVgap(10); grid.setHgap(10); grid.setAlignment(Pos.CENTER);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(txtUser, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(txtPass, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(cboRole, 1, 2);

        getChildren().addAll(new Label("Silver Palm Bank – Login"), grid, btnLogin);
    }

    public String getUsername() { return txtUser.getText().trim(); }
    public String getPassword() { return txtPass.getText(); }
    public String getRole()      { return cboRole.getValue(); }
    public Button getLoginButton(){ return btnLogin; }
}