package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Map;

public class ChartsController {

    @FXML
    private LineChart<String, Number> revenueChart;

    @FXML
    private LineChart<String, Number> expenseChart;

    @FXML
    private PieChart ministryChart;

    @FXML
    private VBox legendBox;

    @FXML
    private ComboBox<Integer> yearComboBox;

    @FXML
    public void initialize() {
        loadRevenueChart();
        loadExpenseChart();

        yearComboBox.getItems().addAll(MinistryBudgetData.getAvailableYears());
        yearComboBox.getSelectionModel().selectFirst();

        yearComboBox.setOnAction(e -> loadMinistryChart());

        loadMinistryChart();
    }

    private void loadRevenueChart() {
        revenueChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Έσοδα");

        BudgetData.getRevenues().forEach((year, map) ->
                series.getData().add(new XYChart.Data<>(String.valueOf(year), BudgetData.getTotalRevenues(year)))
        );

        revenueChart.getData().add(series);
    }

    private void loadExpenseChart() {
        expenseChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Έξοδα");

        BudgetData.getExpenses().forEach((year, map) ->
                series.getData().add(new XYChart.Data<>(String.valueOf(year), BudgetData.getTotalExpenses(year)))
        );

        expenseChart.getData().add(series);
    }

    private void loadMinistryChart() {
        ministryChart.getData().clear();
        legendBox.getChildren().clear();

        int year = yearComboBox.getValue();
        Map<String, Long> ministryExpenses = MinistryBudgetData.getTotalsForYear(year);

        if (ministryExpenses != null) {
            int colorIndex = 0;
            Color[] colors = {Color.CORNFLOWERBLUE, Color.ORANGE, Color.GREEN, Color.RED,
                    Color.PURPLE, Color.BROWN, Color.YELLOW, Color.PINK, Color.DARKCYAN, Color.DARKGOLDENROD};

            for (Map.Entry<String, Long> entry : ministryExpenses.entrySet()) {
                PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue() / 1_000_000_000.0);
                ministryChart.getData().add(slice);

                // Δημιουργούμε legend entry
                HBox legendItem = new HBox(5);
                Rectangle colorBox = new Rectangle(15, 15);
                Color color = colors[colorIndex % colors.length];
                colorBox.setFill(color);
                slice.getNode().setStyle("-fx-pie-color: " + toRgbString(color) + ";");

                Label label = new Label(entry.getKey() + " : " + (entry.getValue() / 1_000_000_000.0) + " B €");
                legendItem.getChildren().addAll(colorBox, label);
                legendBox.getChildren().add(legendItem);

                colorIndex++;
            }
        }
    }

    private String toRgbString(Color c) {
        return String.format("rgb(%d,%d,%d)",
                (int)(c.getRed()*255),
                (int)(c.getGreen()*255),
                (int)(c.getBlue()*255));
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainView.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
