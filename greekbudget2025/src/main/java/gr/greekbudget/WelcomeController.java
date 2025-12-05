package gr.greekbudget;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WelcomeController {

    @FXML
    private void onLoginClicked() {
        System.out.println("Login clicked");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/LoginView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login in");
            stage.setScene(new Scene(root, 400, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSignupClicked() {
        System.out.println("Signup clicked");

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/SignUpView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Create Account");
            stage.setScene(new Scene(root, 400, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
