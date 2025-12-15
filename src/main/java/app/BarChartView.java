package app;

import javafx.scene.chart.*;
import javafx.scene.control.Tab;

public class BarChartView {

    public static Tab createTab() {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(x, y);

        chart.setTitle("Σύγκριση Εσόδων – Εξόδων");

        XYChart.Series<String, Number> revenues = new XYChart.Series<>();
        revenues.setName("Έσοδα");
        revenues.getData().add(new XYChart.Data<>("2025", 1560));

        XYChart.Series<String, Number> expenses = new XYChart.Series<>();
        expenses.setName("Έξοδα");
        expenses.getData().add(new XYChart.Data<>("2025", 1550));

        chart.getData().addAll(revenues, expenses);
        return new Tab("Bar Chart", chart);
    }
}
