package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        // Τα tabs από τις κλάσεις σου
        tabPane.getTabs().add(LineChartView.createTab());
        tabPane.getTabs().add(BarChartView.createTab());

        Scene scene = new Scene(tabPane, 900, 600);
        stage.setTitle("GreekBudget2025 - Graphs");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
