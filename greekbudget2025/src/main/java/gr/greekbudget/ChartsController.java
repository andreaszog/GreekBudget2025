package gr.greekbudget;

import javafx.application.Platform;
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
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Map;

public class ChartsController {

    @FXML private LineChart<String, Number> revenueChart;
    @FXML private LineChart<String, Number> expenseChart;

    @FXML private PieChart ministryChartAll;
    @FXML private PieChart ministryChartNoFinance;

    @FXML private VBox legendBoxAll;
    @FXML private VBox legendBoxNoFinance;

    @FXML private ComboBox<Integer> yearComboBox;

    private Integer highlightedYear;

    // =========================================================
    // INIT
    // =========================================================
    @FXML
    public void initialize() {

        loadRevenueChart();
        loadExpenseChart();

        yearComboBox.getItems().addAll(
                MinistryBudgetData.getAvailableYears()
        );
        yearComboBox.getSelectionModel().selectFirst();
        highlightedYear = yearComboBox.getValue();

        yearComboBox.setOnAction(e -> {
            highlightedYear = yearComboBox.getValue();
            highlightYearOnCharts();
            loadMinistryCharts();
        });

        loadMinistryCharts();
        Platform.runLater(this::highlightYearOnCharts);
    }

    // =========================================================
    // LINE CHARTS
    // =========================================================
    private void loadRevenueChart() {

        revenueChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Έσοδα");

        BudgetData.getRevenues().keySet().stream()
                .sorted()
                .forEach(year ->
                        series.getData().add(
                                new XYChart.Data<>(
                                        String.valueOf(year),
                                        BudgetData.getTotalRevenues(year)
                                )
                        )
                );

        revenueChart.getData().add(series);
    }

    private void loadExpenseChart() {

        expenseChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Έξοδα");

        BudgetData.getExpenses().keySet().stream()
                .sorted()
                .forEach(year ->
                        series.getData().add(
                                new XYChart.Data<>(
                                        String.valueOf(year),
                                        BudgetData.getTotalExpenses(year)
                                )
                        )
                );

        expenseChart.getData().add(series);
    }

    private void highlightYearOnCharts() {
        highlightSeriesPoint(revenueChart, "Έσοδα");
        highlightSeriesPoint(expenseChart, "Έξοδα");
    }

    private void highlightSeriesPoint(LineChart<String, Number> chart, String label) {

        if (highlightedYear == null || chart.getData().isEmpty()) return;

        XYChart.Series<String, Number> series = chart.getData().get(0);

        for (XYChart.Data<String, Number> data : series.getData()) {

            Node node = data.getNode();
            if (node == null) continue;

            node.setStyle("-fx-background-color: #ff8c00;");

            Tooltip tooltip = new Tooltip(
                    "Έτος: " + data.getXValue() + "\n" +
                    label + ": " +
                    String.format("%,d €", data.getYValue().longValue())
            );
            tooltip.setShowDelay(Duration.millis(100));
            Tooltip.install(node, tooltip);

            if (data.getXValue().equals(String.valueOf(highlightedYear))) {
                node.setStyle("-fx-background-color: black;");
            }
        }
    }

    // =========================================================
    // PIE CHARTS
    // =========================================================
    private void loadMinistryCharts() {

        int year = yearComboBox.getValue();
        Map<String, Long> data =
                MinistryBudgetData.getTotalsForYear(year);

        if (data == null) return;

        loadPieChart(ministryChartAll, legendBoxAll, data, false);
        loadPieChart(ministryChartNoFinance, legendBoxNoFinance, data, true);
    }

    private void loadPieChart(
            PieChart chart,
            VBox legendBox,
            Map<String, Long> data,
            boolean excludeFinance
    ) {

        chart.getData().clear();
        legendBox.getChildren().clear();

        Color[] colors = {
                Color.CORNFLOWERBLUE, Color.ORANGE, Color.GREEN,
                Color.RED, Color.PURPLE, Color.BROWN,
                Color.GOLD, Color.TEAL, Color.DARKCYAN
        };

        int colorIndex = 0;

        for (Map.Entry<String, Long> entry : data.entrySet()) {

            if (excludeFinance &&
                entry.getKey().toLowerCase().contains("οικονομ")) {
                continue;
            }

            double valueBillion = entry.getValue() / 1_000_000_000.0;
            PieChart.Data slice =
                    new PieChart.Data(entry.getKey(), valueBillion);

            chart.getData().add(slice);

            Color color = colors[colorIndex % colors.length];

            Platform.runLater(() -> {
                Node node = slice.getNode();

                // Χρώμα slice
                node.setStyle(
                        "-fx-pie-color: " + toRgb(color)
                );

                // Tooltip
                Tooltip tooltip = new Tooltip(
                        entry.getKey() + "\n" +
                        "Σύνολο: " + String.format("%,d €", entry.getValue())
                );
                tooltip.setShowDelay(Duration.millis(120));
                tooltip.setHideDelay(Duration.millis(50));

                Tooltip.install(node, tooltip);
            });


            Rectangle rect = new Rectangle(14, 14, color);
            Label label = new Label(
                    entry.getKey() + " : " +
                    String.format("%.2f B €", valueBillion)
            );

            legendBox.getChildren().add(
                    new HBox(8, rect, label)
            );

            colorIndex++;
        }
    }

    private String toRgb(Color c) {
        return String.format(
                "rgb(%d,%d,%d)",
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255)
        );
    }

    // =========================================================
    // BACK
    // =========================================================
    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root =
                    FXMLLoader.load(getClass().getResource("/MainView.fxml"));

            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/app.css").toExternalForm()
            );

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
