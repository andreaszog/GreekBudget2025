package gr.greekbudget;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import gr.greekbudget.export.ExportContentProvider;
import gr.greekbudget.export.ExportDialogController;
import gr.greekbudget.export.ExportOptions;
import gr.greekbudget.export.PdfExportService;
import gr.greekbudget.export.ChartSnapshotUtil;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import java.awt.image.BufferedImage;

import java.io.File;
import java.util.*;

public class ChartsController implements ExportContentProvider {

    // ================= LINE CHARTS =================
    @FXML private LineChart<String, Number> revenueChart;
    @FXML private LineChart<String, Number> expenseChart;

    // ================= PIE CHARTS =================
    @FXML private PieChart ministryChartAll;
    @FXML private PieChart ministryChartNoFinance;

    @FXML private VBox legendBoxAll;
    @FXML private VBox legendBoxNoFinance;

    // ================= TREND BAR CHART =================
    @FXML private ComboBox<String> trendMinistryComboBox;
    @FXML private BarChart<String, Number> ministryTrendChart;
    @FXML private NumberAxis trendYAxis;
    @FXML private Label trendHintLabel;

    // ================= COMMON =================
    @FXML private ComboBox<Integer> yearComboBox;
    private Integer highlightedYear;

    @FXML private Button trendResetButton;

    // (κρατάμε το id όπως είναι στο FXML σου)
    @FXML private HBox budgetDonutBox;

    // =========================================================
    // ✅ NEW: NICER TOOLTIPS FOR PIE SLICES (PROJECT THEME)
    // =========================================================
    private Tooltip createNiceTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);

        tooltip.setShowDelay(Duration.millis(120));
        tooltip.setHideDelay(Duration.millis(80));

        // theme-friendly, readable
        tooltip.setStyle(
                "-fx-background-color: #fff7e6;" +
                "-fx-text-fill: #4a2600;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 10 8 10;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #f0a500;" +
                "-fx-border-radius: 8;" +
                "-fx-border-width: 1;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 8, 0.3, 0, 2);"
        );

        return tooltip;
    }

    private void installNiceTooltip(Node node, String text) {
        if (node == null) return;
        Tooltip.install(node, createNiceTooltip(text));
    }

    // =========================================================
    // INIT
    // =========================================================
    @FXML
    public void initialize() {

        loadRevenueChart();
        loadExpenseChart();
        loadBudgetBarCharts();

        yearComboBox.getItems().addAll(MinistryBudgetData.getAvailableYears());
        yearComboBox.getSelectionModel().selectFirst();
        highlightedYear = yearComboBox.getValue();

        yearComboBox.setOnAction(e -> {
            highlightedYear = yearComboBox.getValue();
            highlightYearOnCharts();
            loadMinistryCharts();
        });

        loadMinistryCharts();
        setupTrendSection();

        trendResetButton.setOnAction(e -> resetTrendSection());

        Platform.runLater(this::highlightYearOnCharts);
    }

    private void resetTrendSection() {
        trendMinistryComboBox.getSelectionModel().clearSelection();

        ministryTrendChart.getData().clear();
        ministryTrendChart.setVisible(false);
        ministryTrendChart.setManaged(false);

        trendHintLabel.setVisible(true);
        trendHintLabel.setManaged(true);
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
    // BUDGET MINI BAR CHARTS (5 years)
    // =========================================================
    private void loadBudgetBarCharts() {

        budgetDonutBox.getChildren().clear();

        BudgetData.getRevenues().keySet().stream()
                .sorted()
                .forEach(year -> budgetDonutBox.getChildren().add(createYearBars(year)));
    }

    private VBox createYearBars(int year) {

        long revenues = BudgetData.getTotalRevenues(year);
        long expenses = BudgetData.getTotalExpenses(year);
        long result   = BudgetData.getBudgetResult(year);

        // =========================
        // AXES
        // =========================
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setMinorTickVisible(false);

        // 🔧 Tick formatter (μόνο παρουσίαση)
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number value) {
                double display = value.doubleValue();
                if (year == 2025) {
                    display -= 1.0; // 👉 μόνο αυτό ζήτησες
                }
                return String.format("%.0f B €", display);
            }
        });

        // =========================
        // BAR CHART
        // =========================
        BarChart<String, Number> barChart =
                new BarChart<>(xAxis, yAxis);

        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        barChart.setCategoryGap(14);
        barChart.setBarGap(10);
        barChart.setPrefSize(190, 360);

        barChart.setHorizontalGridLinesVisible(true);
        barChart.setVerticalGridLinesVisible(false);
        barChart.setStyle("-fx-background-color: transparent;");

        // =========================
        // DATA (BILLIONS €)
        // =========================
        double revB = revenues / 1_000_000_000.0;
        double expB = expenses / 1_000_000_000.0;

        XYChart.Data<String, Number> revData =
                new XYChart.Data<>("", revB);
        XYChart.Data<String, Number> expData =
                new XYChart.Data<>("", expB);

        XYChart.Series<String, Number> revSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> expSeries = new XYChart.Series<>();

        revSeries.getData().add(revData);
        expSeries.getData().add(expData);

        barChart.getData().addAll(revSeries, expSeries);

        // =========================
        // Y AXIS RANGE (per year)
        // =========================
        double min = Math.min(revB, expB);
        double max = Math.max(revB, expB);

        double range = max - min;
        if (range <= 0) range = max * 0.05;

        double padding = range * 0.45;

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(Math.max(0, min - padding));
        yAxis.setUpperBound(max + padding);
        yAxis.setTickUnit(
                (yAxis.getUpperBound() - yAxis.getLowerBound()) / 4
        );

        // =========================
        // COLORS + TOOLTIPS (ΣΤΑΘΕΡΑ)
        // =========================
        Platform.runLater(() -> {

            Node revNode = revData.getNode();
            if (revNode != null) {
                revNode.setStyle("-fx-bar-fill: #1e90ff;");
                Tooltip t = new Tooltip(
                        String.format("Έσοδα\n%,d €", revenues)
                );
                t.setShowDelay(Duration.millis(120));
                Tooltip.install(revNode, t);
            }

            Node expNode = expData.getNode();
            if (expNode != null) {
                expNode.setStyle("-fx-bar-fill: #e74c3c;");
                Tooltip t = new Tooltip(
                        String.format("Έξοδα\n%,d €", expenses)
                );
                t.setShowDelay(Duration.millis(120));
                Tooltip.install(expNode, t);
            }
        });

        // =========================
        // LABELS
        // =========================
        Label yearLabel = new Label("Έτος " + year);
        yearLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label resultLabel = new Label(formatResult(result));
        resultLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        if (result > 0)
            resultLabel.setTextFill(Color.FORESTGREEN);
        else if (result < 0)
            resultLabel.setTextFill(Color.FIREBRICK);
        else
            resultLabel.setTextFill(Color.GRAY);

        VBox box = new VBox(8, barChart, yearLabel, resultLabel);
        box.setAlignment(Pos.CENTER);

        return box;
    }

    private String formatResult(long value) {

        String status;

        if (value > 0)
            status = "Πλεονασματικός";
        else if (value < 0)
            status = "Ελλειμματικός";
        else
            status = "Ισοσκελισμένος";

        return String.format("%,d € (%s)", value, status);
    }

    private void installBarTooltipWhenReady(
            XYChart.Data<String, Number> data,
            String color,
            Tooltip tooltip
    ) {
        Runnable attach = () -> {
            Node node = data.getNode();
            if (node == null) return;

            node.setStyle("-fx-bar-fill: " + color + ";");

            tooltip.setShowDelay(Duration.millis(120));
            tooltip.setHideDelay(Duration.millis(80));

            Tooltip.uninstall(node, tooltip);
            Tooltip.install(node, tooltip);
        };

        // 1) ASAP
        Platform.runLater(attach);
        // 2) after one more layout pass (crucial for BarChart)
        Platform.runLater(() -> Platform.runLater(attach));
    }

    // =========================================================
    // PIE CHARTS (Ministries)
    // =========================================================
    private void loadMinistryCharts() {

        int year = yearComboBox.getValue();
        Map<String, Long> data = MinistryBudgetData.getTotalsForYear(year);

        if (data == null) return;

        loadPie(ministryChartAll, legendBoxAll, data, false);
        loadPie(ministryChartNoFinance, legendBoxNoFinance, data, true);
    }

    private void loadPie(
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
            PieChart.Data slice = new PieChart.Data(entry.getKey(), valueBillion);

            chart.getData().add(slice);
            Color color = colors[colorIndex % colors.length];

            Platform.runLater(() -> {

                Node node = slice.getNode();
                if (node == null) return;

                node.setStyle("-fx-pie-color: " + toRgb(color));

                // ✅ CHANGED: Nice readable tooltip (instead of default dark one)
                installNiceTooltip(
                        node,
                        entry.getKey() + "\n" +
                        String.format("%,d €", entry.getValue())
                );
            });

            Rectangle rect = new Rectangle(14, 14, color);
            Label label = new Label(
                    entry.getKey() + " : " +
                    String.format("%.2f B €", valueBillion)
            );

            legendBox.getChildren().add(new HBox(8, rect, label));
            colorIndex++;
        }
    }

    @Override
    public BufferedImage getRevenueChartImage() {
        return ChartSnapshotUtil.snapshot(revenueChart);
    }

    @Override
    public BufferedImage getExpenseChartImage() {
        return ChartSnapshotUtil.snapshot(expenseChart);
    }

    @Override
    public BufferedImage getSummaryBarsImage() {
        return ChartSnapshotUtil.snapshot(budgetDonutBox);
    }

    @Override
    public BufferedImage getPieAllImage() {
        return ChartSnapshotUtil.snapshot(ministryChartAll);
    }

    @Override
    public BufferedImage getPieNoFinanceImage() {
        return ChartSnapshotUtil.snapshot(ministryChartNoFinance);
    }

    @Override
    public BufferedImage getMinistryTrendImage(String ministry) {
        return ChartSnapshotUtil.snapshot(ministryTrendChart);
    }

    // =========================================================
    // TREND BAR CHART (Ministry over time)
    // =========================================================
    private void setupTrendSection() {

        int year = yearComboBox.getValue();
        Map<String, Long> data = MinistryBudgetData.getTotalsForYear(year);

        if (data == null) return;

        data.keySet().stream()
                .filter(name -> !name.toLowerCase().contains("οικονομ"))
                .sorted()
                .forEach(trendMinistryComboBox.getItems()::add);

        trendMinistryComboBox.setOnAction(e -> {
            String ministry = trendMinistryComboBox.getValue();
            if (ministry != null) {
                loadTrendChart(ministry);
            }
        });
    }

    private Optional<ExportOptions> showExportDialog() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/ExportDialog.fxml"));

            Parent root = loader.load();
            ExportDialogController controller = loader.getController();

            // ⬅️ ΠΟΛΥ ΣΗΜΑΝΤΙΚΟ
            controller.setYear(yearComboBox.getValue());

            Stage stage = new Stage();
            stage.setTitle("Export to PDF");

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.initOwner(yearComboBox.getScene().getWindow());
            stage.setMaximized(true);     // 🔥 FULL SCREEN
            stage.showAndWait();

            return Optional.ofNullable(controller.getResult());

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @FXML
    private void exportPdf() {

        // 1️⃣ Άνοιγμα dialog επιλογής τι θα γίνει export
        Optional<ExportOptions> optionsOpt = showExportDialog();
        if (optionsOpt.isEmpty()) {
            return;
        }

        // 2️⃣ File chooser για αποθήκευση PDF
        FileChooser fc = new FileChooser();
        fc.setTitle("Αποθήκευση PDF");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        Stage stage = (Stage) yearComboBox.getScene().getWindow();
        File file = fc.showSaveDialog(stage);

        if (file == null) {
            return;
        }

        // 3️⃣ Αν δεν τελειώνει σε .pdf → το προσθέτουμε
        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            file = new File(file.getAbsolutePath() + ".pdf");
        }

        // 4️⃣ Δημιουργία PDF
        try {
            PdfExportService.export(file, optionsOpt.get(), this);
        } catch (Exception ex) {
            ex.printStackTrace();

            new Alert(
                    Alert.AlertType.ERROR,
                    "Σφάλμα κατά τη δημιουργία PDF\n\n"
                            + ex.getClass().getSimpleName() + "\n"
                            + (ex.getMessage() != null ? ex.getMessage() : "")
            ).showAndWait();
        }
    }

    private void loadTrendChart(String ministry) {

        ministryTrendChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(ministry);

        List<Long> values = new ArrayList<>();

        for (Integer year : MinistryBudgetData.getAvailableYears().stream().sorted().toList()) {
            Map<String, Long> yearData = MinistryBudgetData.getTotalsForYear(year);
            if (yearData == null) continue;

            Long value = yearData.get(ministry);
            if (value == null) continue;

            series.getData().add(new XYChart.Data<>(String.valueOf(year), value));
            values.add(value);
        }

        ministryTrendChart.getData().add(series);
        applyTightYAxis(values);
        styleTrendBars(series);

        trendHintLabel.setVisible(false);
        trendHintLabel.setManaged(false);
        ministryTrendChart.setVisible(true);
        ministryTrendChart.setManaged(true);
    }

    private void applyTightYAxis(List<Long> values) {

        if (values.isEmpty()) {
            trendYAxis.setAutoRanging(true);
            return;
        }

        long min = values.stream().min(Long::compare).orElse(0L);
        long max = values.stream().max(Long::compare).orElse(0L);

        double range = max - min;
        if (range <= 0) range = max * 0.1;

        double padding = range * 0.15;

        trendYAxis.setAutoRanging(false);
        trendYAxis.setLowerBound(Math.max(0, min - padding));
        trendYAxis.setUpperBound(max + padding);
        trendYAxis.setTickUnit(
                (trendYAxis.getUpperBound() - trendYAxis.getLowerBound()) / 5
        );
    }

    private void styleTrendBars(XYChart.Series<String, Number> series) {

        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> d : series.getData()) {

                Node node = d.getNode();
                if (node == null) continue;

                node.setStyle("-fx-bar-fill: #ff8c00;");

                Tooltip tooltip = new Tooltip(
                        series.getName() + "\n" +
                                "Έτος: " + d.getXValue() + "\n" +
                                "Ποσό: " +
                                String.format("%,d €", d.getYValue().longValue())
                );
                tooltip.setShowDelay(Duration.millis(100));
                Tooltip.install(node, tooltip);
            }
        });
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
            Parent root = FXMLLoader.load(getClass().getResource("/MainView.fxml"));

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.getScene().setRoot(root);
            stage.setTitle("Dashboard");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
