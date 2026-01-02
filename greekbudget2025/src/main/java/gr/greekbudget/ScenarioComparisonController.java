package gr.greekbudget;


import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

public class ScenarioComparisonController {

    // ===== TABLE =====
    @FXML private TableView<ScenarioComparisonRow> table;
    @FXML private TableColumn<ScenarioComparisonRow, Number> yearCol;
    @FXML private TableColumn<ScenarioComparisonRow, String> ministryCol;
    @FXML private TableColumn<ScenarioComparisonRow, Number> oldCol;
    @FXML private TableColumn<ScenarioComparisonRow, Number> newCol;
    @FXML private TableColumn<ScenarioComparisonRow, Number> deltaCol;
    @FXML private TableColumn<ScenarioComparisonRow, Number> deltaPctCol;

    // ===== SUMMARY =====
    @FXML private VBox budgetImpactBox;

    // ===== KPI =====
    @FXML private Label kpiChangedMinistries;
    @FXML private Label kpiTotalDelta;
    @FXML private Label kpiFinalResult;
    @FXML private Label kpiStatus;

    // ===== CHARTS =====
    @FXML private BarChart<String, Number> amountChart;
    @FXML private BarChart<String, Number> deltaChart;
    @FXML private PieChart pieBefore;
    @FXML private PieChart pieAfter;

    private final ObservableList<ScenarioComparisonRow> rows =
            FXCollections.observableArrayList();

    // κρατάμε τελευταία impacts για KPI
    private List<BudgetImpactRow> lastImpacts = List.of();

    // ================= INITIALIZE =================
    @FXML
    private void initialize() {

        table.setItems(rows);

        yearCol.setCellValueFactory(c -> c.getValue().yearProperty());
        ministryCol.setCellValueFactory(c -> c.getValue().ministryProperty());
        oldCol.setCellValueFactory(c -> c.getValue().oldAmountProperty());
        newCol.setCellValueFactory(c -> c.getValue().newAmountProperty());
        deltaCol.setCellValueFactory(c -> c.getValue().deltaProperty());
        deltaPctCol.setCellValueFactory(c -> c.getValue().deltaPctProperty());

        oldCol.setCellFactory(col -> moneyCell());
        newCol.setCellFactory(col -> moneyCell());
        deltaCol.setCellFactory(col -> moneyCellSigned());

        deltaPctCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : String.format("%.2f%%", v.doubleValue()));
            }
        });

        // nicer autosize (όπως είχες)
        table.setFixedCellSize(28);
        table.prefHeightProperty().bind(
                table.fixedCellSizeProperty()
                        .multiply(Bindings.size(table.getItems()).add(1.2))
        );

        setupCharts();
    }

    private void setupCharts() {
        // amountChart
        amountChart.setAnimated(false);
        amountChart.setLegendVisible(true);
        amountChart.setCategoryGap(18);
        amountChart.setBarGap(6);

        // deltaChart
        deltaChart.setAnimated(false);
        deltaChart.setLegendVisible(false);
        deltaChart.setCategoryGap(18);
        deltaChart.setBarGap(6);

        // pie
        pieBefore.setLabelsVisible(false);
        pieAfter.setLabelsVisible(false);
    }

    // ================= DATA FROM PREVIOUS SCREEN =================
    public void setData(List<ScenarioComparisonRow> comparisonRows, List<BudgetImpactRow> impacts) {
        rows.setAll(comparisonRows);
        lastImpacts = impacts != null ? impacts : List.of();

        buildSummaryBox(lastImpacts);
        buildKpis(rows, lastImpacts);
        buildCharts(rows);
    }

    private void buildSummaryBox(List<BudgetImpactRow> impacts) {
        budgetImpactBox.getChildren().clear();
        if (impacts == null) return;

        for (BudgetImpactRow r : impacts) {

            Label yearLbl = new Label("ΕΤΟΣ " + r.getYear());
            yearLbl.setStyle("-fx-text-fill:white; -fx-font-weight:bold;");

            Label beforeLbl = new Label(
                    "ΑΠΟΤΕΛΕΣΜΑ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ΠΡΙΝ ΤΙΣ ΤΡΟΠΟΠΟΙΗΣΕΙΣ: "
                            + MoneyUtil.formatSignedEuro(r.getBefore())
            );
            beforeLbl.setStyle("-fx-text-fill:white;");

            Label afterLbl = new Label(
                    "ΑΠΟΤΕΛΕΣΜΑ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ΜΕΤΑ ΤΙΣ ΤΡΟΠΟΠΟΙΗΣΕΙΣ: "
                            + MoneyUtil.formatSignedEuro(r.getAfter())
            );
            afterLbl.setStyle("-fx-text-fill:white;");

            long finalResult = r.getAfter();

            String status;
            if (finalResult > 0) status = "ΠΛΕΟΝΑΣΜΑΤΙΚΟΣ";
            else if (finalResult < 0) status = "ΕΛΛΕΙΜΜΑΤΙΚΟΣ";
            else status = "ΙΣΟΣΚΕΛΙΣΜΕΝΟΣ";

            Label diffLbl = new Label(
                    "ΑΠΟΤΕΛΕΣΜΑ ΚΡΑΤΙΚΟΥ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ (ΕΣΟΔΑ - ΕΞΟΔΑ): "
                            + MoneyUtil.formatSignedEuro(finalResult)
                            + "  →  "
                            + status
            );
            diffLbl.setStyle("-fx-text-fill:white; -fx-font-weight:bold;");

            Separator midSep = new Separator();
            midSep.setOpacity(0.6);

            budgetImpactBox.getChildren().addAll(
                    yearLbl,
                    beforeLbl,
                    afterLbl,
                    midSep,
                    diffLbl,
                    new Separator()
            );
        }
    }

    private void buildKpis(List<ScenarioComparisonRow> rows,
                       List<BudgetImpactRow> impacts) {

        Map<Integer, List<ScenarioComparisonRow>> byYear =
                rows.stream().collect(Collectors.groupingBy(ScenarioComparisonRow::getYear));

        StringBuilder deltaTxt = new StringBuilder();
        StringBuilder resultTxt = new StringBuilder();
        StringBuilder statusTxt = new StringBuilder();

        byYear.forEach((year, list) -> {

            long totalDelta = list.stream()
                    .mapToLong(ScenarioComparisonRow::getDelta)
                    .sum();

            Optional<BudgetImpactRow> impact =
                    impacts.stream().filter(i -> i.getYear() == year).findFirst();

            long after = impact.map(BudgetImpactRow::getAfter).orElse(0L);

            String status;
            if (after > 0) status = "ΠΛΕΟΝΑΣΜΑ";
            else if (after < 0) status = "ΕΛΛΕΙΜΜΑ";
            else status = "ΙΣΟΣΚΕΛΙΣΜΕΝΟ";

            deltaTxt.append("Έτος ").append(year).append(": ")
                    .append(MoneyUtil.formatSignedEuro(totalDelta)).append("\n");

            resultTxt.append("Έτος ").append(year).append(": ")
                    .append(MoneyUtil.formatSignedEuro(after)).append("\n");

            statusTxt.append("Έτος ").append(year).append(": ")
                    .append(status).append("\n");
        });

        kpiChangedMinistries.setText(
                String.valueOf(rows.stream().filter(r -> r.getDelta() != 0).count())
        );

        kpiTotalDelta.setText(deltaTxt.toString());
        kpiFinalResult.setText(resultTxt.toString());
        kpiStatus.setText(statusTxt.toString());
    }


    private void buildCharts(List<ScenarioComparisonRow> rows) {

        // ========= BAR: old vs new =========
        amountChart.getData().clear();

        XYChart.Series<String, Number> oldSeries = new XYChart.Series<>();
        oldSeries.setName("Παλαιό");

        XYChart.Series<String, Number> newSeries = new XYChart.Series<>();
        newSeries.setName("Νέο");

        // sort by absolute delta (top changes first) για καλύτερο visual
        List<ScenarioComparisonRow> sorted = rows.stream()
                .sorted(Comparator.comparingLong((ScenarioComparisonRow r) -> Math.abs(r.getDelta())).reversed())
                .collect(Collectors.toList());

        for (ScenarioComparisonRow r : sorted) {
            oldSeries.getData().add(new XYChart.Data<>(shortMinistry(r.getMinistry()), r.getOldAmount()));
            newSeries.getData().add(new XYChart.Data<>(shortMinistry(r.getMinistry()), r.getNewAmount()));
        }

        amountChart.getData().addAll(oldSeries, newSeries);

        // tooltips
        Platform.runLater(() -> {
            installBarTooltips(oldSeries, "Παλαιό");
            installBarTooltips(newSeries, "Νέο");
        });

        // ========= BAR: delta =========
        deltaChart.getData().clear();

        XYChart.Series<String, Number> deltaSeries = new XYChart.Series<>();
        for (ScenarioComparisonRow r : sorted) {
            deltaSeries.getData().add(new XYChart.Data<>(shortMinistry(r.getMinistry()), r.getDelta()));
        }
        deltaChart.getData().add(deltaSeries);

        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> d : deltaSeries.getData()) {
                Node node = d.getNode();
                if (node == null) continue;

                long val = d.getYValue().longValue();
                if (val < 0) node.getStyleClass().add("bar-negative");
                else if (val > 0) node.getStyleClass().add("bar-positive");

                Tooltip t = new Tooltip(
                        "Υπουργείο: " + d.getXValue() + "\n" +
                                "Μεταβολή: " + MoneyUtil.formatSignedEuro(val)
                );
                t.setShowDelay(Duration.millis(100));
                Tooltip.install(node, t);
            }
        });

        // ========= PIE: before/after (Top N + Others) =========
        buildPieTopN(pieBefore, rows, true, 7);
        buildPieTopN(pieAfter, rows, false, 7);
    }

    private void buildPieTopN(PieChart chart, List<ScenarioComparisonRow> rows, boolean useOld, int topN) {
        chart.getData().clear();

        List<ScenarioComparisonRow> sorted = rows.stream()
                .sorted(Comparator.comparingLong((ScenarioComparisonRow r) -> useOld ? r.getOldAmount() : r.getNewAmount()).reversed())
                .collect(Collectors.toList());

        long total = sorted.stream().mapToLong(r -> useOld ? r.getOldAmount() : r.getNewAmount()).sum();

        long topSum = 0;
        for (int i = 0; i < sorted.size(); i++) {
            ScenarioComparisonRow r = sorted.get(i);
            long value = useOld ? r.getOldAmount() : r.getNewAmount();

            if (i < topN) {
                topSum += value;
                PieChart.Data slice = new PieChart.Data(shortMinistry(r.getMinistry()), value);
                chart.getData().add(slice);
            }
        }

        long others = Math.max(0, total - topSum);
        if (others > 0) {
            chart.getData().add(new PieChart.Data("Λοιπά", others));
        }

        Platform.runLater(() -> {
            for (PieChart.Data d : chart.getData()) {
                Node node = d.getNode();
                if (node == null) continue;

                long v = (long) d.getPieValue();
                double pct = total == 0 ? 0 : (v * 100.0 / total);

                Tooltip t = new Tooltip(
                        d.getName() + "\n" +
                                MoneyUtil.formatEuro(v) + "\n" +
                                String.format("%.2f%%", pct)
                );
                t.setShowDelay(Duration.millis(100));
                Tooltip.install(node, t);
            }
        });
    }

    private void installBarTooltips(XYChart.Series<String, Number> series, String label) {
        for (XYChart.Data<String, Number> d : series.getData()) {
            Node node = d.getNode();
            if (node == null) continue;

            Tooltip t = new Tooltip(
                    "Υπουργείο: " + d.getXValue() + "\n" +
                            label + ": " + MoneyUtil.formatEuro(d.getYValue().longValue())
            );
            t.setShowDelay(Duration.millis(100));
            Tooltip.install(node, t);
        }
    }

    private String shortMinistry(String name) {
        if (name == null) return "";
        String n = name.trim();
        // κρατάμε το “Υπουργείο …” αλλά κόβουμε μήκος για να χωράει στα charts
        if (n.length() <= 22) return n;
        return n.substring(0, 22) + "…";
    }

    // ================= BACK =================
    @FXML
    private void goBack(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MinistryScenariosView.fxml"));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Σενάρια Υπουργικών Δαπανών");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ================= CELL HELPERS =================
    private TableCell<ScenarioComparisonRow, Number> moneyCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : MoneyUtil.formatEuro(v.longValue()));
            }
        };
    }

    private TableCell<ScenarioComparisonRow, Number> moneyCellSigned() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : MoneyUtil.formatSignedEuro(v.longValue()));
            }
        };
    }

}
