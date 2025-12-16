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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChartsController {

    private static final double BILLION = 1_000_000_000.0;

    // =========================================================
    // ΧΕΙΡΟΚΙΝΗΤΑ, ΣΤΑΘΕΡΑ ΧΡΩΜΑΤΑ ΓΙΑ ΤΑ ΒΑΣΙΚΑ ΥΠΟΥΡΓΕΙΑ
    // =========================================================
    private static final Map<String, Color> MINISTRY_COLORS = Map.ofEntries(
            Map.entry("Υπουργείο Εθνικής Οικονομίας και Οικονομικών", Color.DARKSLATEGRAY),
            Map.entry("Υπουργείο Εσωτερικών", Color.CORNFLOWERBLUE),
            Map.entry("Υπουργείο Εθνικής Άμυνας", Color.DARKGREEN),
            Map.entry("Υπουργείο Υγείας", Color.INDIANRED),
            Map.entry("Υπουργείο Παιδείας", Color.DARKORANGE),
            Map.entry("Υπουργείο Εργασίας και Κοινωνικών Υποθέσεων", Color.TEAL),
            Map.entry("Υπουργείο Υποδομών και Μεταφορών", Color.DARKBLUE),
            Map.entry("Υπουργείο Περιβάλλοντος και Ενέργειας", Color.OLIVE),
            Map.entry("Υπουργείο Πολιτισμού", Color.GOLDENROD),
            Map.entry("Υπουργείο Μετανάστευσης και Ασύλου", Color.MEDIUMPURPLE)
    );

    // =========================================================
    // ΠΑΛΕΤΑ ΓΙΑ ΤΑ ΥΠΟΛΟΙΠΑ (ΣΤΑΘΕΡΗ – ΟΧΙ RANDOM)
    // =========================================================
    private static final Color[] FALLBACK_PALETTE = {
            Color.SKYBLUE,
            Color.LIGHTGREEN,
            Color.LIGHTCORAL,
            Color.KHAKI,
            Color.PLUM,
            Color.PEACHPUFF,
            Color.LIGHTSEAGREEN,
            Color.LIGHTSALMON,
            Color.LIGHTSTEELBLUE,
            Color.LIGHTPINK,
            Color.BURLYWOOD,
            Color.PALEGOLDENROD
    };

    // ================= FXML =================
    @FXML private LineChart<String, Number> revenueChart;
    @FXML private LineChart<String, Number> expenseChart;
    @FXML private PieChart ministryChart;
    @FXML private VBox legendBox;
    @FXML private ComboBox<Integer> yearComboBox;

    // ================= INIT =================
    @FXML
    public void initialize() {

        ministryChart.setLegendVisible(false);

        loadRevenueChart();
        loadExpenseChart();

        yearComboBox.getItems().addAll(
                MinistryBudgetData.getAvailableYears()
        );
        yearComboBox.getSelectionModel().selectFirst();
        yearComboBox.setOnAction(e -> loadMinistryChart());

        loadMinistryChart();
    }

    // ================= LINE CHARTS =================
    private void loadRevenueChart() {
        revenueChart.getData().clear();

        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Έσοδα");

        BudgetData.getRevenues().keySet().stream()
                .sorted()
                .forEach(year ->
                        s.getData().add(
                                new XYChart.Data<>(
                                        String.valueOf(year),
                                        BudgetData.getTotalRevenues(year)
                                )
                        )
                );

        revenueChart.getData().add(s);
    }

    private void loadExpenseChart() {
        expenseChart.getData().clear();

        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Έξοδα");

        BudgetData.getExpenses().keySet().stream()
                .sorted()
                .forEach(year ->
                        s.getData().add(
                                new XYChart.Data<>(
                                        String.valueOf(year),
                                        BudgetData.getTotalExpenses(year)
                                )
                        )
                );

        expenseChart.getData().add(s);
    }

    // ================= PIE CHART =================
    private void loadMinistryChart() {

        ministryChart.getData().clear();
        legendBox.getChildren().clear();

        int year = yearComboBox.getValue();
        Map<String, Long> raw =
                MinistryBudgetData.getTotalsForYear(year);

        if (raw == null) return;

        // 1️⃣ AGGREGATION ΑΝΑ CANONICAL ΥΠΟΥΡΓΕΙΟ
        Map<String, Long> aggregated = new LinkedHashMap<>();

        raw.forEach((name, value) -> {
            String canonical =
                    MinistryNameNormalizer.canonical(name);
            aggregated.merge(canonical, value, Long::sum);
        });

        // 2️⃣ PIE DATA
        aggregated.forEach((ministry, value) ->
                ministryChart.getData().add(
                        new PieChart.Data(
                                ministry,
                                value / BILLION
                        )
                )
        );

        // 3️⃣ ΧΡΩΜΑΤΑ + LEGEND ΜΕΤΑ ΤΟ LAYOUT
        Platform.runLater(() -> {

            legendBox.getChildren().clear();

            for (PieChart.Data slice : ministryChart.getData()) {

                String ministry = slice.getName();
                Color color = getColorForMinistry(ministry);

                if (slice.getNode() != null) {
                    slice.getNode().setStyle(
                            "-fx-pie-color: " + toRgb(color) + ";"
                    );
                }

                Rectangle rect = new Rectangle(15, 15, color);

                Label label = new Label(
                        MinistryNameNormalizer.displayLabel(ministry)
                                + " : "
                                + String.format("%.2f", slice.getPieValue())
                                + " B €"
                );

                legendBox.getChildren().add(
                        new HBox(8, rect, label)
                );
            }
        });
    }

    // ================= COLOR LOGIC =================
    private Color getColorForMinistry(String ministry) {

        // Αν έχουμε χειροκίνητο χρώμα → παίρνουμε αυτό
        if (MINISTRY_COLORS.containsKey(ministry)) {
            return MINISTRY_COLORS.get(ministry);
        }

        // Αλλιώς: σταθερό fallback από hash
        int index = Math.abs(ministry.hashCode()) % FALLBACK_PALETTE.length;
        return FALLBACK_PALETTE[index];
    }

    private String toRgb(Color c) {
        return String.format(
                "rgb(%d,%d,%d)",
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255)
        );
    }

    // ================= BACK =================
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
                    (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
