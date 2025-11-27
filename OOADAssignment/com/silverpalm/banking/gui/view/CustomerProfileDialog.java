package OOADAssignment.com.silverpalm.banking.gui.view;

import OOADAssignment.com.silverpalm.banking.core.model.Customer;
import OOADAssignment.com.silverpalm.banking.core.service.BankingService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomerProfileDialog extends Stage {
    public CustomerProfileDialog(String customerId) {
        setTitle("Customer Profile – " + customerId);
        initModality(Modality.APPLICATION_MODAL);

        VBox vb = new VBox(10);
        vb.setPadding(new Insets(15));
        TextArea text = new TextArea();
        text.setEditable(false);
        vb.getChildren().addAll(new Label("Profile Details:"), text);

        Customer c = BankingService.getInstance().getCustomer(customerId);
        text.setText(c == null ? "Customer not found" : c.toString());

        setScene(new Scene(vb, 400, 300));
    }
}