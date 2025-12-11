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

public class MainController {

    @FXML
    private Label usernameLabel;

    private String username;

    public void setUsername(String username) {
        this.username = username;
        if (usernameLabel != null) {
            usernameLabel.setText("Logged in as: " + username);
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(yes, no);

        alert.showAndWait().ifPresent(result -> {
            if (result == yes) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/WelcomeView.fxml"));
                    Scene scene = new Scene(root, 400, 400);
                    scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

                    Stage stage = (Stage) usernameLabel.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void openSummary() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/SummaryView.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    // OPEN ANALYSIS VIEW — no setData(), no Summary loading
    // ======================================================
    @FXML
    private void openAnalysis() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AnalysisView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Ανάλυση Κρατικού Προϋπολογισμού");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR loading AnalysisView");
        }
    }
}
