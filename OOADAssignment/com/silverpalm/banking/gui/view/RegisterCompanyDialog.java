package OOADAssignment.com.silverpalm.banking.gui.view;

import OOADAssignment.com.silverpalm.banking.core.service.BankingService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegisterCompanyDialog extends Stage {
    public RegisterCompanyDialog() {
        setTitle("Register Company Customer");
        initModality(Modality.APPLICATION_MODAL);

        GridPane gp = new GridPane();
        gp.setHgap(10); gp.setVgap(10); gp.setPadding(new Insets(15));

        TextField user = new TextField();
        PasswordField pass = new PasswordField();
        TextField comp = new TextField();
        TextField reg  = new TextField();
        TextField contact= new TextField();
        TextField addr = new TextField();
        TextField phone= new TextField();
        TextField email= new TextField();
        TextField branch=new TextField();

        Button btnSave = new Button("Save");
        Button btnCancel= new Button("Cancel");

        gp.add(new Label("Username"), 0, 0);   gp.add(user, 1, 0);
        gp.add(new Label("Password"), 0, 1);   gp.add(pass, 1, 1);
        gp.add(new Label("Company Name"), 0, 2); gp.add(comp, 1, 2);
        gp.add(new Label("Registration No"), 0, 3); gp.add(reg, 1, 3);
        gp.add(new Label("Contact Person"), 0, 4); gp.add(contact, 1, 4);
        gp.add(new Label("Address"), 0, 5);    gp.add(addr, 1, 5);
        gp.add(new Label("Phone"), 0, 6);      gp.add(phone, 1, 6);
        gp.add(new Label("Email"), 0, 7);      gp.add(email, 1, 7);
        gp.add(new Label("Branch"), 0, 8);     gp.add(branch, 1, 8);
        gp.add(btnSave, 0, 9);                 gp.add(btnCancel, 1, 9);

        btnSave.setOnAction(e -> {
            try {
                BankingService.getInstance().createCompanyCustomer(
                        user.getText(), pass.getText(), comp.getText(), reg.getText(),
                        contact.getText(), addr.getText(), phone.getText(),
                        email.getText(), branch.getText());
                new Alert(Alert.AlertType.INFORMATION, "Company customer registered").showAndWait();
                close();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });
        btnCancel.setOnAction(ev -> close());

        setScene(new Scene(gp, 400, 380));
    }
}