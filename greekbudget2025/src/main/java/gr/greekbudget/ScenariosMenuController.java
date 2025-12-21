package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class ScenariosMenuController {

    @FXML
    private void openRevenueScenarios(ActionEvent e) {
        load(e, "/RevExpScenarios.fxml", "Σενάρια Εσόδων - Εξόδων");
    }

    @FXML
    private void openMinistryScenarios(ActionEvent e) {
        load(e, "/MinistryScenariosView.fxml", "Σενάρια Υπουργικών Δαπανών");
    }

    @FXML
    private void openLoanScenarios(ActionEvent e) {
        load(e, "/LoanScenariosView.fxml", "Σενάρια Δανείων");
    }

    @FXML
    private void openAllScenarios(ActionEvent e) {
        load(e, "/AllScenariosView.fxml", "Πολλαπλά Σενάρια");
    }

    @FXML
    private void goBack(ActionEvent e) {
        load(e, "/MainView.fxml", "Dashboard");
    }

    // ======================
    // SINGLE CORRECT LOADER
    // ======================
    private void load(ActionEvent e, String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource(fxml)
            );

            Stage stage = (Stage) ((Node) e.getSource())
                    .getScene()
                    .getWindow();

            stage.getScene().setRoot(root);   // ✅ ΜΟΝΟ ROOT
            stage.setTitle(title);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
