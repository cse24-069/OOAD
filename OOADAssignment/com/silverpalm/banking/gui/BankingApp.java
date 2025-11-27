package OOADAssignment.com.silverpalm.banking.gui;

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

    /* =====================  NAVIGATION API  ===================== */
    public static void showLogin() {
        root.getChildren().clear();
        var view = new OOADAssignment.com.silverpalm.banking.gui.view.LoginView();
        new OOADAssignment.com.silverpalm.banking.gui.controller.LoginController(view);
        root.getChildren().add(view);
    }

    public static void showCustomerDash(String customerId) {
        root.getChildren().clear();
        var view = new OOADAssignment.com.silverpalm.banking.gui.view.CustomerDashboardView(customerId);
        new OOADAssignment.com.silverpalm.banking.gui.controller.CustomerDashboardController(view);
        root.getChildren().add(view);
    }

    /*  FIXED  –  supplies the teller-id the constructor now demands  */
    public static void showTellerDash() {
        root.getChildren().clear();
        String tellerId = OOADAssignment.com.silverpalm.banking.core.service.BankingService
                .getInstance()
                .getBankTeller()
                .getTellerId();
        var view = new OOADAssignment.com.silverpalm.banking.gui.view.TellerDashboardView(tellerId);
        new OOADAssignment.com.silverpalm.banking.gui.controller.TellerDashboardController(view);
        root.getChildren().add(view);
    }

    public static void logout() { showLogin(); }

    /*  helpers expected by TellerDashboardController  */
    public static void showDepositDialog(String customerId) {
        new OOADAssignment.com.silverpalm.banking.gui.view.DepositDialog(customerId).showAndWait();
    }
    public static void showWithdrawDialog(String customerId) {
        new OOADAssignment.com.silverpalm.banking.gui.view.WithdrawDialog(customerId).showAndWait();
    }
    public static void showTransactionHistory(String customerId) {
        new OOADAssignment.com.silverpalm.banking.gui.view.TransactionHistoryDialog(customerId).showAndWait();
    }
    public static void showCustomerProfile(String customerId) {
        new OOADAssignment.com.silverpalm.banking.gui.view.CustomerProfileDialog(customerId).showAndWait();
    }
}