package gr.greekbudget;

import gr.greekbudget.database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignUpController {

    @FXML private TextField usernameField;

    // Password
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;
    @FXML private Button togglePasswordButton;
    private boolean passwordVisible = false;

    // Confirm password
    @FXML private PasswordField confirmField;
    @FXML private TextField confirmVisibleField;
    @FXML private Button toggleConfirmButton;
    private boolean confirmVisible = false;


    @FXML
    private void togglePassword() {

        if (passwordVisible) {
            passwordField.setText(passwordVisibleField.getText());
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            togglePasswordButton.setText("◎"); // hide
            passwordVisible = false;

        } else {
            passwordVisibleField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);
            togglePasswordButton.setText("◉"); // show
            passwordVisible = true;
        }
    }

    @FXML
    private void toggleConfirmPassword() {

        if (confirmVisible) {
            confirmField.setText(confirmVisibleField.getText());
            confirmVisibleField.setVisible(false);
            confirmVisibleField.setManaged(false);
            confirmField.setVisible(true);
            confirmField.setManaged(true);
            toggleConfirmButton.setText("◎"); // hide
            confirmVisible = false;

        } else {
            confirmVisibleField.setText(confirmField.getText());
            confirmField.setVisible(false);
            confirmField.setManaged(false);
            confirmVisibleField.setVisible(true);
            confirmVisibleField.setManaged(true);
            toggleConfirmButton.setText("◉"); // show
            confirmVisible = true;
        }
    }


    @FXML
    private void register() {

        String username = usernameField.getText().trim();

        String pass = passwordVisible ? passwordVisibleField.getText().trim()
                                      : passwordField.getText().trim();

        String confirm = confirmVisible ? confirmVisibleField.getText().trim()
                                        : confirmField.getText().trim();

        if (username.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            showAlert("Error", "Please fill all fields!");
            return;
        }

        if (!pass.equals(confirm)) {
            showAlert("Error", "Passwords do not match!");
            return;
        }

        try (Connection conn = Database.getConnection()) {

            PreparedStatement check = conn.prepareStatement(
                    "SELECT id FROM users WHERE username = ?"
            );
            check.setString(1, username);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                showAlert("Error", "Username already exists!");
                return;
            }

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
