package gr.greekbudget;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.util.*;

public class MinistryScenariosController {

    @FXML private ComboBox<Integer> yearBox;
    @FXML private Button compareBtn;

    @FXML private TableView<MinistryRow> table;
    @FXML private TableColumn<MinistryRow, Boolean> selectCol;
    @FXML private TableColumn<MinistryRow, String> nameCol;
    @FXML private TableColumn<MinistryRow, Number> amountCol;

    @FXML private TableView<ScenarioRow> selectedTable;
    @FXML private TableColumn<ScenarioRow, Number> selYearCol;
    @FXML private TableColumn<ScenarioRow, String> selNameCol;
    @FXML private TableColumn<ScenarioRow, Number> oldAmountCol;
    @FXML private TableColumn<ScenarioRow, Long> newAmountCol;

    private final ObservableList<MinistryRow> allRows =
            FXCollections.observableArrayList();
    private final ObservableList<ScenarioRow> scenarioRows =
            FXCollections.observableArrayList();

    private final Map<String, ScenarioRow> scenarioByKey = new HashMap<>();
    private final Map<Integer, Set<String>> selectedByYear = new HashMap<>();

    private final NumberFormat fmt =
            NumberFormat.getInstance(new Locale("el", "GR"));

    // ===============================
    // INITIALIZE
    // ===============================
    @FXML
    public void initialize() {

        List<Integer> years = new ArrayList<>(MinistryBudgetData.getAvailableYears());
        years.sort(Comparator.reverseOrder());
        yearBox.setItems(FXCollections.observableArrayList(years));
        yearBox.setValue(2026);
        yearBox.valueProperty().addListener((o, a, b) -> loadYear(b));

        table.setItems(allRows);
        table.setEditable(true);

        selectCol.setCellValueFactory(c -> c.getValue().selectedProperty());
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));

        nameCol.setCellValueFactory(c -> c.getValue().ministryProperty());
        amountCol.setCellValueFactory(c -> c.getValue().amountProperty());

        amountCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : fmt.format(v.longValue()));
            }
        });

        selectedTable.setItems(scenarioRows);
        selYearCol.setCellValueFactory(c -> c.getValue().yearProperty());
        selNameCol.setCellValueFactory(c -> c.getValue().ministryProperty());
        oldAmountCol.setCellValueFactory(c -> c.getValue().oldAmountProperty());
        newAmountCol.setCellValueFactory(c -> c.getValue().newAmountProperty());
        newAmountCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongConverter()));

        loadYear(2026);
    }

    // ===============================
    // LOAD YEAR
    // ===============================
    private void loadYear(int year) {
        allRows.clear();

        Map<String, Long> data = MinistryBudgetData.getTotalsForYear(year);
        if (data == null) return;

        Set<String> selected =
                selectedByYear.computeIfAbsent(year, y -> new HashSet<>());

        for (var e : data.entrySet()) {
            MinistryRow r = new MinistryRow(year, e.getKey(), e.getValue());
            r.setSelected(selected.contains(e.getKey()));

            r.selectedProperty().addListener((obs, o, n) -> {
                if (n) addScenario(r);
                else removeScenario(r);
                compareBtn.setDisable(scenarioRows.isEmpty());
            });

            allRows.add(r);
        }
    }

    private void addScenario(MinistryRow r) {
        ScenarioRow sr = new ScenarioRow(r.getYear(), r.getMinistry(), r.getAmount());
        scenarioByKey.put(r.key(), sr);
        scenarioRows.add(sr);
    }

    private void removeScenario(MinistryRow r) {
        ScenarioRow sr = scenarioByKey.remove(r.key());
        if (sr != null) scenarioRows.remove(sr);
    }

    // ===============================
    // COMPARE
    // ===============================
    @FXML
    private void compare(ActionEvent e) {

        for (ScenarioRow r : scenarioRows) {
            if (r.getNewAmount() == null) {
                new Alert(Alert.AlertType.WARNING,
                        "Συμπλήρωσε νέο ποσό για όλα τα υπουργεία.")
                        .showAndWait();
                return;
            }
        }

        ObservableList<ScenarioComparisonRow> comparisonRows =
                FXCollections.observableArrayList();

        for (ScenarioRow r : scenarioRows) {
            comparisonRows.add(
                    new ScenarioComparisonRow(
                            r.getYear(),
                            r.getMinistry(),
                            r.getOldAmount(),
                            r.getNewAmount()
                    )
            );
        }

        Map<Integer, Long> deltaPerYear = new HashMap<>();
        for (ScenarioComparisonRow r : comparisonRows) {
            deltaPerYear.merge(r.getYear(), r.getDelta(), Long::sum);
        }

        List<BudgetImpactRow> impacts = new ArrayList<>();
        for (var e2 : deltaPerYear.entrySet()) {
            long before = BudgetData.getBudgetResult(e2.getKey());
            long after = before - e2.getValue();
            impacts.add(new BudgetImpactRow(e2.getKey(), before, after));
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ScenarioComparisonView.fxml")
            );
            Parent root = loader.load();

            ScenarioComparisonController ctrl = loader.getController();
            ctrl.setData(comparisonRows, impacts);

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Σύγκριση Σεναρίου");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ===============================
    // CONVERTER
    // ===============================
    private static class LongConverter extends StringConverter<Long> {
        @Override public String toString(Long v) { return v == null ? "" : v.toString(); }
        @Override public Long fromString(String s) {
            if (s == null || s.isBlank()) return null;
            return Long.parseLong(s.replace(".", "").replace(",", ""));
        }
    }

    @FXML
    private void goBack(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/ScenariosView.fxml")
            );

            Stage stage = (Stage) ((Node) e.getSource())
                    .getScene()
                    .getWindow();

            stage.getScene().setRoot(root);
            stage.setTitle("Σενάρια");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}