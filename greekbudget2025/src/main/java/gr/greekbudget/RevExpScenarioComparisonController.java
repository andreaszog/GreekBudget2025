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

public class RevExpScenarioComparisonController {

    // ===== TABLE =====
    @FXML private TableView<RevExpScenarioComparisonRow> table;
    @FXML private TableColumn<RevExpScenarioComparisonRow, Number> yearCol;
    @FXML private TableColumn<RevExpScenarioComparisonRow, String> typeCol;
    @FXML private TableColumn<RevExpScenarioComparisonRow, String> nameCol;
    @FXML private TableColumn<RevExpScenarioComparisonRow, Number> oldCol;
    @FXML private TableColumn<RevExpScenarioComparisonRow, Number> newCol;
    @FXML private TableColumn<RevExpScenarioComparisonRow, Number> deltaCol;
    @FXML private TableColumn<RevExpScenarioComparisonRow, Number> deltaPctCol;

    // ===== SUMMARY =====
    @FXML private VBox budgetImpactBox;

    // ===== KPI =====
    @FXML private Label kpiChangedCategories;
    @FXML private Label kpiRevenueDelta;
    @FXML private Label kpiExpenseDelta;
    @FXML private Label kpiFinalResult;

    // ===== CHARTS =====
    @FXML private BarChart<String, Number> amountChart;
    @FXML private BarChart<String, Number> deltaChart;
    @FXML private PieChart pieBefore;
    @FXML private PieChart pieAfter;

    private final ObservableList<RevExpScenarioComparisonRow> rows =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        table.setItems(rows);

        yearCol.setCellValueFactory(c -> c.getValue().yearProperty());
        typeCol.setCellValueFactory(c -> c.getValue().typeProperty());
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
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

        // ίδιο “nicer autosize”
        table.setFixedCellSize(28);
        table.prefHeightProperty().bind(
                table.fixedCellSizeProperty()
                        .multiply(Bindings.size(table.getItems()).add(1.2))
        );

        setupCharts();
    }

    private void setupCharts() {
        amountChart.setAnimated(false);
        amountChart.setLegendVisible(true);
        amountChart.setCategoryGap(18);
        amountChart.setBarGap(6);

        deltaChart.setAnimated(false);
        deltaChart.setLegendVisible(false);
        deltaChart.setCategoryGap(18);
        deltaChart.setBarGap(6);

        pieBefore.setLabelsVisible(false);
        pieAfter.setLabelsVisible(false);
    }

    // =======================
    // DATA FROM PREVIOUS SCREEN
    // =======================
    public void setData(List<RevExpScenarioComparisonRow> data) {
        rows.setAll(data);

        buildImpactBox(rows);
        buildKpis(rows);
        buildCharts(rows);
    }

    // =======================
    // KPI + IMPACT
    // =======================
    private void buildKpis(List<RevExpScenarioComparisonRow> rows) {

        kpiChangedCategories.setText(
                String.valueOf(rows.stream().filter(r -> r.getDelta() != 0).count())
        );

        Map<Integer, List<RevExpScenarioComparisonRow>> byYear =
                rows.stream().collect(Collectors.groupingBy(RevExpScenarioComparisonRow::getYear));

        StringBuilder revTxt = new StringBuilder();
        StringBuilder expTxt = new StringBuilder();
        StringBuilder resTxt = new StringBuilder();

        byYear.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {

                    int year = e.getKey();
                    List<RevExpScenarioComparisonRow> list = e.getValue();

                    long revDelta = list.stream()
                            .filter(this::isRevenue)
                            .mapToLong(RevExpScenarioComparisonRow::getDelta)
                            .sum();

                    long expDelta = list.stream()
                            .filter(this::isExpense)
                            .mapToLong(RevExpScenarioComparisonRow::getDelta)
                            .sum();

                    long before = BudgetData.getBudgetResult(year);

                    // RESULT = (revenues - expenses)
                    // if revenues increase => result increases
                    // if expenses increase => result decreases
                    long after = before + revDelta - expDelta;

                    revTxt.append("Έτος ").append(year).append(": ")
                            .append(MoneyUtil.formatSignedEuro(revDelta)).append("\n");

                    expTxt.append("Έτος ").append(year).append(": ")
                            .append(MoneyUtil.formatSignedEuro(expDelta)).append("\n");

                    resTxt.append("Έτος ").append(year).append(": ")
                            .append(MoneyUtil.formatSignedEuro(after)).append("\n");
                });

        kpiRevenueDelta.setText(revTxt.toString());
        kpiExpenseDelta.setText(expTxt.toString());
        kpiFinalResult.setText(resTxt.toString());
    }

    private void buildImpactBox(List<RevExpScenarioComparisonRow> rows) {

        budgetImpactBox.getChildren().clear();

        Map<Integer, List<RevExpScenarioComparisonRow>> byYear =
                rows.stream().collect(Collectors.groupingBy(RevExpScenarioComparisonRow::getYear));

        byYear.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {

                    int year = e.getKey();
                    List<RevExpScenarioComparisonRow> list = e.getValue();

                    long revDelta = list.stream()
                            .filter(this::isRevenue)
                            .mapToLong(RevExpScenarioComparisonRow::getDelta)
                            .sum();

                    long expDelta = list.stream()
                            .filter(this::isExpense)
                            .mapToLong(RevExpScenarioComparisonRow::getDelta)
                            .sum();

                    long before = BudgetData.getBudgetResult(year);
                    long after = before + revDelta - expDelta;

                    Label yearLbl = new Label("ΕΤΟΣ " + year);
                    yearLbl.setStyle("-fx-text-fill:white; -fx-font-weight:bold;");

                    Label beforeLbl = new Label(
                            "ΑΠΟΤΕΛΕΣΜΑ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ΠΡΙΝ: " + MoneyUtil.formatSignedEuro(before)
                    );
                    beforeLbl.setStyle("-fx-text-fill:white;");

                    Label revLbl = new Label(
                            "ΜΕΤΑΒΟΛΗ ΕΣΟΔΩΝ (σύνολο): " + MoneyUtil.formatSignedEuro(revDelta)
                    );
                    revLbl.setStyle("-fx-text-fill:white;");

                    Label expLbl = new Label(
                            "ΜΕΤΑΒΟΛΗ ΕΞΟΔΩΝ (σύνολο): " + MoneyUtil.formatSignedEuro(expDelta)
                    );
                    expLbl.setStyle("-fx-text-fill:white;");

                    String status;
                    if (after > 0) status = "ΠΛΕΟΝΑΣΜΑΤΙΚΟΣ";
                    else if (after < 0) status = "ΕΛΛΕΙΜΜΑΤΙΚΟΣ";
                    else status = "ΙΣΟΣΚΕΛΙΣΜΕΝΟΣ";

                    Label afterLbl = new Label(
                            "ΑΠΟΤΕΛΕΣΜΑ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ΜΕΤΑ: "
                                    + MoneyUtil.formatSignedEuro(after)
                                    + "  →  " + status
                    );
                    afterLbl.setStyle("-fx-text-fill:white; -fx-font-weight:bold;");

                    Separator midSep = new Separator();
                    midSep.setOpacity(0.6);

                    budgetImpactBox.getChildren().addAll(
                            yearLbl,
                            beforeLbl,
                            revLbl,
                            expLbl,
                            midSep,
                            afterLbl,
                            new Separator()
                    );
                });
    }

    private boolean isRevenue(RevExpScenarioComparisonRow r) {
        String t = r.getType() == null ? "" : r.getType().toLowerCase();
        return t.contains("έσοδ") || t.contains("εσοδ") || t.contains("revenue");
    }

    private boolean isExpense(RevExpScenarioComparisonRow r) {
        String t = r.getType() == null ? "" : r.getType().toLowerCase();
        return t.contains("έξοδ") || t.contains("εξοδ") || t.contains("expense");
    }

    // =======================
    // CHARTS
    // =======================
    private void buildCharts(List<RevExpScenarioComparisonRow> rows) {

        // ========= BAR: old vs new =========
        amountChart.getData().clear();

        XYChart.Series<String, Number> oldSeries = new XYChart.Series<>();
        oldSeries.setName("Παλαιό");

        XYChart.Series<String, Number> newSeries = new XYChart.Series<>();
        newSeries.setName("Νέο");

        List<RevExpScenarioComparisonRow> sorted = rows.stream()
                .sorted(Comparator.comparingLong((RevExpScenarioComparisonRow r) -> Math.abs(r.getDelta())).reversed())
                .toList();

        for (RevExpScenarioComparisonRow r : sorted) {
            String label = shortLabel(r);
            oldSeries.getData().add(new XYChart.Data<>(label, r.getOldAmount()));
            newSeries.getData().add(new XYChart.Data<>(label, r.getNewAmount()));
        }

        amountChart.getData().addAll(oldSeries, newSeries);

        Platform.runLater(() -> {
            installBarTooltips(oldSeries, "Παλαιό");
            installBarTooltips(newSeries, "Νέο");
        });

        // ========= BAR: delta =========
        deltaChart.getData().clear();

        XYChart.Series<String, Number> deltaSeries = new XYChart.Series<>();
        for (RevExpScenarioComparisonRow r : sorted) {
            deltaSeries.getData().add(new XYChart.Data<>(shortLabel(r), r.getDelta()));
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
                        d.getXValue() + "\n" +
                        "Μεταβολή: " + MoneyUtil.formatSignedEuro(val)
                );
                t.setShowDelay(Duration.millis(100));
                Tooltip.install(node, t);
            }
        });

        // ========= PIE: Top N + Others =========
        buildPieTopN(pieBefore, rows, true, 7);
        buildPieTopN(pieAfter, rows, false, 7);
    }

    private void buildPieTopN(PieChart chart, List<RevExpScenarioComparisonRow> rows, boolean useOld, int topN) {
        chart.getData().clear();

        List<RevExpScenarioComparisonRow> sorted = rows.stream()
                .sorted(Comparator.comparingLong((RevExpScenarioComparisonRow r) -> useOld ? r.getOldAmount() : r.getNewAmount()).reversed())
                .toList();

        long total = sorted.stream().mapToLong(r -> useOld ? r.getOldAmount() : r.getNewAmount()).sum();

        long topSum = 0;
        for (int i = 0; i < sorted.size(); i++) {
            RevExpScenarioComparisonRow r = sorted.get(i);
            long value = useOld ? r.getOldAmount() : r.getNewAmount();

            if (i < topN) {
                topSum += value;
                chart.getData().add(new PieChart.Data(shortLabel(r), value));
            }
        }

        long others = Math.max(0, total - topSum);
        if (others > 0) chart.getData().add(new PieChart.Data("Λοιπά", others));

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
                    d.getXValue() + "\n" +
                    label + ": " + MoneyUtil.formatEuro(d.getYValue().longValue())
            );
            t.setShowDelay(Duration.millis(100));
            Tooltip.install(node, t);
        }
    }

    private String shortLabel(RevExpScenarioComparisonRow r) {
        String t = r.getType() == null ? "" : r.getType().trim();
        String n = r.getName() == null ? "" : r.getName().trim();

        String full = (t.isBlank() ? "" : (t + ": ")) + n;
        if (full.length() <= 26) return full;
        return full.substring(0, 26) + "…";
    }

    // ================= BACK =================
    @FXML
    private void goBack(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/RevExpScenarios.fxml"));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Σενάρια Εσόδων - Εξόδων");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ================= CELL HELPERS =================
    private TableCell<RevExpScenarioComparisonRow, Number> moneyCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : MoneyUtil.formatEuro(v.longValue()));
            }
        };
    }

    private TableCell<RevExpScenarioComparisonRow, Number> moneyCellSigned() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : MoneyUtil.formatSignedEuro(v.longValue()));
            }
        };
    }
}
