package com.silverpalm.banking.gui.view;

import com.silverpalm.banking.core.model.AccountType;
import com.silverpalm.banking.core.model.CustomerType;
import com.silverpalm.banking.core.service.BankingService;
import com.silverpalm.banking.core.exception.BankingException;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OpenAccountDialog extends Stage {
    private final String customerId;

    public OpenAccountDialog(String customerId) {
        this.customerId = customerId;
        setTitle("Open Account – " + customerId);
        initModality(Modality.APPLICATION_MODAL);

        GridPane gp = new GridPane();
        gp.setHgap(10); gp.setVgap(10); gp.setPadding(new Insets(15));

        ComboBox<AccountType> cboType = new ComboBox<>();
        cboType.getItems().setAll(AccountType.SAVINGS, AccountType.INVESTMENT, AccountType.CHEQUE);

        TextField txtDeposit = new TextField();
        TextField txtBranch  = new TextField();
        TextField txtEmployer= new TextField();
        TextField txtEmpAddr = new TextField();

        Button btnSave = new Button("Open");
        Button btnCancel= new Button("Cancel");

        gp.add(new Label("Account Type"), 0, 0); gp.add(cboType, 1, 0);
        gp.add(new Label("Initial Deposit"), 0, 1); gp.add(txtDeposit, 1, 1);
        gp.add(new Label("Branch"), 0, 2); gp.add(txtBranch, 1, 2);

        // employer fields visible only for cheque
        gp.add(new Label("Employer Name"), 0, 3);  gp.add(txtEmployer, 1, 3);
        gp.add(new Label("Employer Address"), 0, 4); gp.add(txtEmpAddr, 1, 4);
        txtEmployer.setVisible(false); txtEmpAddr.setVisible(false);

        cboType.setOnAction(e -> {
            boolean cheque = cboType.getValue() == AccountType.CHEQUE;
            txtEmployer.setVisible(cheque); txtEmpAddr.setVisible(cheque);
        });

        gp.add(btnSave, 0, 5); gp.add(btnCancel, 1, 5);

        btnSave.setOnAction(e -> {
            try {
                double amt = Double.parseDouble(txtDeposit.getText());
                String branch = txtBranch.getText();
                AccountType type = cboType.getValue();

                switch (type) {
                    case SAVINGS:
                        BankingService.getInstance().openSavingsAccount(customerId, amt, branch);
                        break;
                    case INVESTMENT:
                        BankingService.getInstance().openInvestmentAccount(customerId, amt, branch);
                        break;
                    case CHEQUE:
                        BankingService.getInstance().openChequeAccount(customerId, amt, branch,
                                txtEmployer.getText(), txtEmpAddr.getText());
                        break;
                }
                new Alert(Alert.AlertType.INFORMATION, "Account opened").showAndWait();
                close();
            } catch (BankingException | NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });
        btnCancel.setOnAction(ev -> close());

        setScene(new Scene(gp, 420, 270));
    }
}