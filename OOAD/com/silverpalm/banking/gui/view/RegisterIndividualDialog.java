package com.silverpalm.banking.gui.view;

import com.silverpalm.banking.core.service.BankingService;
import com.silverpalm.banking.core.model.Gender;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class RegisterIndividualDialog extends Stage {
    public RegisterIndividualDialog() {
        setTitle("Register Individual Customer");
        initModality(Modality.APPLICATION_MODAL);

        GridPane gp = new GridPane();
        gp.setHgap(10); gp.setVgap(10); gp.setPadding(new Insets(15));

        TextField user = new TextField();
        PasswordField pass = new PasswordField();
        TextField first = new TextField();
        TextField last  = new TextField();
        DatePicker dob  = new DatePicker();
        ComboBox<Gender> gender = new ComboBox<>();
        gender.getItems().setAll(Gender.values());
        TextField id    = new TextField();
        TextField addr  = new TextField();
        TextField phone = new TextField();
        TextField email = new TextField();
        TextField branch= new TextField();

        Button btnSave = new Button("Save");
        Button btnCancel= new Button("Cancel");

        gp.add(new Label("Username"), 0, 0);   gp.add(user, 1, 0);
        gp.add(new Label("Password"), 0, 1);   gp.add(pass, 1, 1);
        gp.add(new Label("First Name"), 0, 2); gp.add(first, 1, 2);
        gp.add(new Label("Last Name"), 0, 3);  gp.add(last, 1, 3);
        gp.add(new Label("Date of Birth"), 0, 4); gp.add(dob, 1, 4);
        gp.add(new Label("Gender"), 0, 5);     gp.add(gender, 1, 5);
        gp.add(new Label("ID Number (9 digits)"), 0, 6); gp.add(id, 1, 6);
        gp.add(new Label("Address"), 0, 7);    gp.add(addr, 1, 7);
        gp.add(new Label("Phone"), 0, 8);      gp.add(phone, 1, 8);
        gp.add(new Label("Email"), 0, 9);      gp.add(email, 1, 9);
        gp.add(new Label("Branch"), 0, 10);    gp.add(branch, 1, 10);
        gp.add(btnSave, 0, 11);                gp.add(btnCancel, 1, 11);

        btnSave.setOnAction(e -> {
            try {
                BankingService.getInstance().createIndividualCustomer(
                        user.getText(), pass.getText(), first.getText(), last.getText(),
                        dob.getValue(), gender.getValue(), id.getText(), addr.getText(),
                        phone.getText(), email.getText(), branch.getText());
                new Alert(Alert.AlertType.INFORMATION, "Individual customer registered").showAndWait();
                close();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });
        btnCancel.setOnAction(ev -> close());

        setScene(new Scene(gp, 400, 450));
    }
}