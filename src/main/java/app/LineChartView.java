package app;

import javafx.scene.chart.*;
import javafx.scene.control.Tab;

public class LineChartView {

    public static Tab createTab() {
        NumberAxis x = new NumberAxis(2020, 2025, 1);
        NumberAxis y = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(x, y);

        chart.setTitle("Εξέλιξη Προϋπολογισμού");

        XYChart.Series<Number, Number> balance = new XYChart.Series<>();
        balance.setName("Ισοζύγιο");

        balance.getData().add(new XYChart.Data<>(2020, -15));
        balance.getData().add(new XYChart.Data<>(2021, -10));
        balance.getData().add(new XYChart.Data<>(2022, -5));
        balance.getData().add(new XYChart.Data<>(2023, 2));
        balance.getData().add(new XYChart.Data<>(2024, 5));
        balance.getData().add(new XYChart.Data<>(2025, 10));

        chart.getData().add(balance);
        return new Tab("Line Chart", chart);
    }
}
