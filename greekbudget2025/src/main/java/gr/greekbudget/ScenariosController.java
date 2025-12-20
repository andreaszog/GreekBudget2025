package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ScenariosController {

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/MainView.fxml")
            );

            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/app.css").toExternalForm()
            );

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene().getWindow();

            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
