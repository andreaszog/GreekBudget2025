package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
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
    private void goBack(javafx.event.ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/MainView.fxml")
            );

            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/app.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) e.getSource())
                    .getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void load(ActionEvent e, String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource(fxml)
            );

            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/app.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
