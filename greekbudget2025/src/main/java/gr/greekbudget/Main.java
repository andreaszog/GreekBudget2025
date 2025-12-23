package gr.greekbudget;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(
                getClass().getResource("/WelcomeView.fxml")
        );

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/styles/app.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setTitle("Greek Budget");
        stage.setMaximized(true);   // ✅ ΣΩΣΤΟ ΣΗΜΕΙΟ
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
