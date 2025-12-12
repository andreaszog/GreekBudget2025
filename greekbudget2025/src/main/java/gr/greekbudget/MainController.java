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
        usernameLabel.setText("Logged in as: " + username);
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

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/WelcomeView.fxml"));
                    Scene scene = new Scene(root, 400, 400);
                    scene.getStylesheets().add(
                            getClass().getResource("/styles/app.css").toExternalForm()
                    );

                    Stage stage = (Stage) usernameLabel.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // ======================
    // SUMMARY
    // ======================
    @FXML
    private void openSummary() {
        loadView("/SummaryView.fxml", "Δαπάνες / Έσοδα ανά Υπουργείο");
    }

    // ======================
    // ANALYSIS
    // ======================
    @FXML
    private void openAnalysis() {
        loadView("/AnalysisView.fxml", "Ανάλυση Προϋπολογισμού");
    }

    // ======================
    // INCOME - EXPENSE
    // ======================
    @FXML
    private void openIncomeExpense() {
        loadView("/IncomeExpenseView.fxml", "Έσοδα - Έξοδα");
    }

    // ======================
    // GENERIC LOADER
    // ======================
    private void loadView(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/app.css").toExternalForm()
            );

            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
