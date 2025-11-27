package OOADAssignment.com.silverpalm.banking.gui.view;

import OOADAssignment.com.silverpalm.banking.core.model.Transaction;
import OOADAssignment.com.silverpalm.banking.core.service.BankingService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class TransactionHistoryDialog extends Stage {
    public TransactionHistoryDialog(String customerId) {
        setTitle("Transaction History – " + customerId);
        initModality(Modality.APPLICATION_MODAL);

        VBox vb = new VBox(10);
        vb.setPadding(new Insets(15));

        Label lblAcc = new Label("Account Number:");
        TextField txtAcc = new TextField();
        Button btnGo = new Button("Show");
        ListView<String> list = new ListView<>();

        btnGo.setOnAction(e -> {
            list.getItems().clear();
            List<Transaction> tx =
                    BankingService.getInstance().getRecentAccountTransactions(txtAcc.getText(), 50);
            if (tx.isEmpty()) list.getItems().add("No transactions");
            else tx.forEach(t -> list.getItems().add(t.toString()));
        });

        vb.getChildren().addAll(lblAcc, txtAcc, btnGo, list);
        setScene(new Scene(vb, 500, 400));
    }
}