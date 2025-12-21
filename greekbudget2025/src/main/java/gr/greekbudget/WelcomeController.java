package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class WelcomeController {

    @FXML
    private void onLoginClicked(ActionEvent event) {
        switchView(event, "/LoginView.fxml", "Log In");
    }

    @FXML
    private void onSignupClicked(ActionEvent event) {
        switchView(event, "/SignUpView.fxml", "Create Account");
    }

    // ===== ΚΕΝΤΡΙΚΗ ΜΕΘΟΔΟΣ ΠΛΟΗΓΗΣΗΣ =====
    private void switchView(ActionEvent event, String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.getScene().setRoot(root);   // ⬅ ΤΟ ΣΗΜΑΝΤΙΚΟ
            stage.setTitle(title);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
