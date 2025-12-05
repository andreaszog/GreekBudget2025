package gr.greekbudget;

import gr.greekbudget.database.CreateTables;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // -------------------------
        // 1. Αρχικοποίηση βάσης
        // -------------------------
        CreateTables.initialize();

        // -------------------------
        // 2. Φορτώνουμε την αρχική οθόνη (Welcome)
        // -------------------------
        Parent root = FXMLLoader.load(getClass().getResource("/WelcomeView.fxml"));
        Scene scene = new Scene(root, 400, 400);

        stage.setTitle("Blender Budget");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
