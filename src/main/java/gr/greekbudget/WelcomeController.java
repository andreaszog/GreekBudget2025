package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WelcomeController {

    // Ανοίγει το LoginView ΣΤΟ ΙΔΙΟ ΠΑΡΑΘΥΡΟ
    @FXML
    private void onLoginClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/LoginView.fxml"));
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/app.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Log In");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ανοίγει το SignUpView ΣΤΟ ΙΔΙΟ ΠΑΡΑΘΥΡΟ
    @FXML
    private void onSignupClicked(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/SignUpView.fxml"));
            Scene scene = new Scene(root, 400, 400);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/app.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Create Account");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
