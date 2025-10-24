package com.silverpalm.banking.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BankingApp extends Application {

    private static Stage primaryStage;
    private static StackPane root;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        root = new StackPane();
        stage.setTitle("Silver Palm Bank – JavaFX Client");
        stage.setScene(new Scene(root, 950, 650));
        stage.setResizable(false);
        stage.show();
        showLogin();
    }

    /*  NAVIGATION API  */
    public static void showLogin() {
        root.getChildren().clear();
        var view = new com.silverpalm.banking.gui.view.LoginView();
        new com.silverpalm.banking.gui.controller.LoginController(view);
        root.getChildren().add(view);
    }

    public static void showCustomerDash(String customerId) {
        root.getChildren().clear();
        var view = new com.silverpalm.banking.gui.view.CustomerDashboardView(customerId);
        new com.silverpalm.banking.gui.controller.CustomerDashboardController(view);
        root.getChildren().add(view);
    }

    public static void showTellerDash() {
        root.getChildren().clear();
        var view = new com.silverpalm.banking.gui.view.TellerDashboardView();
        new com.silverpalm.banking.gui.controller.TellerDashboardController(view);
        root.getChildren().add(view);
    }

    public static void logout() { showLogin(); }
}