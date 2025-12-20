package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class MainController {

    @FXML
    private Label usernameLabel;

    private String username;

    // ======================
    // USER
    // ======================
    public void setUsername(String username) {
        this.username = username;
        if (usernameLabel != null) {
            usernameLabel.setText("Logged in as: " + username);
        }
    }

    // ======================
    // LOGOUT
    // ======================
    @FXML
    private void logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(yes, no);

        alert.showAndWait().ifPresent(r -> {
            if (r == yes) {
                loadView(event, "/WelcomeView.fxml", "Welcome", 400, 400);
            }
        });
    }

    // ======================
    // NAVIGATION
    // ======================
    @FXML
    private void openSummary(ActionEvent e) {
        loadView(e, "/SummaryView.fxml", "Δαπάνες ανά Υπουργείο");
    }

    @FXML
    private void openAnalysis(ActionEvent e) {
        loadView(e, "/AnalysisView.fxml", "Ανάλυση Προϋπολογισμού");
    }

    @FXML
    private void openIncomeExpense(ActionEvent e) {
        loadView(e, "/IncomeExpenseView.fxml", "Έσοδα - Έξοδα");
    }

    @FXML
    private void openCharts(ActionEvent e) {
        loadView(e, "/ChartsView.fxml", "Γραφήματα & Στατιστικά");
    }

    @FXML
    private void openScenarios(ActionEvent e) {
        loadView(e, "/ScenariosView.fxml", "Σενάρια");
    }

    // ======================
    // GENERIC LOADER (ΣΩΣΤΟΣ)
    // ======================
    private void loadView(ActionEvent event, String fxml, String title) {
        loadView(event, fxml, title, 800, 600);
    }

    private void loadView(ActionEvent event, String fxml, String title, int w, int h) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root, w, h);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/app.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
