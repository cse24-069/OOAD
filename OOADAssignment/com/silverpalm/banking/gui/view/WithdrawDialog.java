package OOADAssignment.com.silverpalm.banking.gui.view;

import OOADAssignment.com.silverpalm.banking.core.service.BankingService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WithdrawDialog extends Stage {
    public WithdrawDialog(String customerId) {
        setTitle("Withdraw – " + customerId);
        initModality(Modality.APPLICATION_MODAL);

        GridPane gp = new GridPane();
        gp.setHgap(10); gp.setVgap(10); gp.setPadding(new Insets(15));

        Label lblAcc = new Label("Account Number:");
        Label lblAmt = new Label("Amount (BWP):");
        TextField txtAcc = new TextField();
        TextField txtAmt = new TextField();
        Button btnOk   = new Button("Withdraw");
        Button btnCancel = new Button("Cancel");

        gp.add(lblAcc, 0, 0); gp.add(txtAcc, 1, 0);
        gp.add(lblAmt, 0, 1); gp.add(txtAmt, 1, 1);
        gp.add(btnOk, 0, 2);  gp.add(btnCancel, 1, 2);

        btnOk.setOnAction(e -> {
            try {
                BankingService.getInstance().withdraw(txtAcc.getText(), Double.parseDouble(txtAmt.getText()));
                new Alert(Alert.AlertType.INFORMATION, "Withdrawal successful").showAndWait();
                close();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });
        btnCancel.setOnAction(e -> close());

        setScene(new Scene(gp, 320, 140));
    }
}