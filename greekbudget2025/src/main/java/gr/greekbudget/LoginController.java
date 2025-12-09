package gr.greekbudget;

import gr.greekbudget.database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void login() {
        String username = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (username.isEmpty() || pass.isEmpty()) {
            showAlert("Error", "Please fill all fields!");
            return;
        }

        try (Connection conn = Database.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT password FROM users WHERE username = ?"
            );
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                showAlert("Error", "Wrong username or password!");
                return;
            }

            String storedPassword = rs.getString("password");

            if (!storedPassword.equals(pass)) {
                showAlert("Error", "Wrong username or password!");
                return;
            }

            showAlert("Success", "Welcome, " + username + "!");

            URL fxml = getClass().getResource("/MainView.fxml");
            FXMLLoader loader = new FXMLLoader(fxml);
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setUsername(username);

            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Blender Budget - Dashboard");
            stage.setScene(scene);
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Database error! (check console)");
        }
    }

    @FXML
    private void goBack() {
        try {
        Parent root = FXMLLoader.load(getClass().getResource("/WelcomeView.fxml"));

        Scene scene = new Scene(root, 400, 400);
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}