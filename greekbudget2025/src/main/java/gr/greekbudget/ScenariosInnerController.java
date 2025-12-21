package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class ScenariosInnerController {

    public void goBack(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/ScenariosView.fxml")
            );

            Stage stage = (Stage) ((Node) e.getSource())
                    .getScene()
                    .getWindow();

            stage.getScene().setRoot(root);   // ✅ ΜΟΝΟ ROOT
            stage.setTitle("Σενάρια");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
