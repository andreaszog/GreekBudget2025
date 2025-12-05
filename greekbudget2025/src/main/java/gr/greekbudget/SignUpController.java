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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignUpController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;

    // ------------------------
    // REGISTER BUTTON
    // ------------------------
    @FXML
    private void register() {
        String username = usernameField.getText().trim();
        String pass = passwordField.getText().trim();
        String confirm = confirmField.getText().trim();

        // 1️⃣ Empty fields
        if (username.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            showAlert("Error", "Please fill all fields!");
            return;
        }

        // 2️⃣ Passwords match?
        if (!pass.equals(confirm)) {
            showAlert("Error", "Passwords do not match!");
            return;
        }

        try (Connection conn = Database.getConnection()) {

            // 3️⃣ Check if username exists
            PreparedStatement check = conn.prepareStatement(
                    "SELECT id FROM users WHERE username = ?"
            );
            check.setString(1, username);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                showAlert("Error", "Username already exists!");
                return;
            }

            // 4️⃣ Insert new user
            PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO users(username, password) VALUES (?, ?)"
            );
            insert.setString(1, username);
            insert.setString(2, pass);
            insert.executeUpdate();

            showAlert("Success", "Account created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Database error! (check console)");
        }
    }

    // ------------------------
    // BACK BUTTON
    // ------------------------
    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/WelcomeView.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------------
    // Helper popup alert
    // ------------------------
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
