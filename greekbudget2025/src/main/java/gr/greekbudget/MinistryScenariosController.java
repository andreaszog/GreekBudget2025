package gr.greekbudget;

import javafx.beans.value.ChangeListener;
import javafx.collections.*;
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

    @FXML private TableView<MinistryRow> table;
    @FXML private TableColumn<MinistryRow, Boolean> selectCol;
    @FXML private TableColumn<MinistryRow, String> nameCol;
    @FXML private TableColumn<MinistryRow, Number> amountCol;

    @FXML private TableView<ScenarioRow> selectedTable;
    @FXML private TableColumn<ScenarioRow, Number> selYearCol;
    @FXML private TableColumn<ScenarioRow, String> selNameCol;
    @FXML private TableColumn<ScenarioRow, Number> oldAmountCol;
    @FXML private TableColumn<ScenarioRow, Long> newAmountCol;

    @FXML private Button compareBtn;

    private final ObservableList<MinistryRow> allRows = FXCollections.observableArrayList();
    private final ObservableList<ScenarioRow> scenarioRows = FXCollections.observableArrayList();

    private final Map<String, ScenarioRow> scenarioByKey = new LinkedHashMap<>();
    private final Map<Integer, Set<String>> selectedByYear = new HashMap<>();

    private final NumberFormat fmt = NumberFormat.getInstance(new Locale("el", "GR"));

    @FXML
    public void initialize() {

        // ================= YEAR BOX =================
        List<Integer> years = new ArrayList<>(MinistryBudgetData.getAvailableYears());
        years.sort(Comparator.reverseOrder());
        yearBox.setItems(FXCollections.observableArrayList(years));
        yearBox.setValue(2026);

        yearBox.valueProperty().addListener((obs, o, n) -> loadYear(n));

        // ================= LEFT TABLE =================
        table.setEditable(true);              // 🔥 ΑΠΑΡΑΙΤΗΤΟ
        selectCol.setEditable(true);          // 🔥 ΑΠΑΡΑΙΤΗΤΟ
        table.setItems(allRows);

        selectCol.setCellValueFactory(c -> c.getValue().selectedProperty());
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));

        nameCol.setCellValueFactory(c -> c.getValue().ministryProperty());
        amountCol.setCellValueFactory(c -> c.getValue().amountProperty());

        amountCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : fmt.format(v.longValue()));
            }
        });

        // 👉 EXTRA UX: click anywhere on row toggles checkbox
        table.setRowFactory(tv -> {
            TableRow<MinistryRow> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty()) {
                    MinistryRow r = row.getItem();
                    r.setSelected(!r.isSelected());
                }
            });
            return row;
        });

        // ================= RIGHT TABLE =================
        selectedTable.setEditable(true);
        selectedTable.setItems(scenarioRows);

        selYearCol.setCellValueFactory(c -> c.getValue().yearProperty());
        selNameCol.setCellValueFactory(c -> c.getValue().ministryProperty());
        oldAmountCol.setCellValueFactory(c -> c.getValue().oldAmountProperty());

        oldAmountCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : fmt.format(v.longValue()));
            }
        });

        newAmountCol.setCellValueFactory(c -> c.getValue().newAmountProperty());
        newAmountCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongConverter()));

        newAmountCol.setOnEditCommit(e -> {
            e.getRowValue().setNewAmount(e.getNewValue());
            updateCompareButton();
        });

        loadYear(2026);
    }

    private void loadYear(int year) {
        allRows.clear();
        Map<String, Long> data = MinistryBudgetData.getTotalsForYear(year);
        if (data == null) return;

        Set<String> selected = selectedByYear.computeIfAbsent(year, y -> new HashSet<>());

        for (var e : data.entrySet()) {
            MinistryRow row = new MinistryRow(year, e.getKey(), e.getValue());
            row.setSelected(selected.contains(e.getKey()));

            row.selectedProperty().addListener((obs, o, n) -> {
                if (n) {
                    selected.add(e.getKey());
                    addScenario(row);
                } else {
                    selected.remove(e.getKey());
                    removeScenario(row);
                }
                updateCompareButton();
            });

            allRows.add(row);
        }
    }

    private void addScenario(MinistryRow r) {
        String key = r.key();
        if (scenarioByKey.containsKey(key)) return;

        ScenarioRow sr = new ScenarioRow(r.getYear(), r.getMinistry(), r.getAmount());
        scenarioByKey.put(key, sr);
        scenarioRows.add(sr);
    }

    private void removeScenario(MinistryRow r) {
        ScenarioRow sr = scenarioByKey.remove(r.key());
        if (sr != null) scenarioRows.remove(sr);
    }

    private void updateCompareButton() {
        compareBtn.setDisable(scenarioRows.isEmpty());
    }

    @FXML
    private void compare(ActionEvent e) {
        System.out.println("COMPARE clicked");
    }

    @FXML
    private void goBack(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ScenariosView.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ================= CONVERTER =================
    private static class LongConverter extends StringConverter<Long> {
        @Override public String toString(Long v) { return v == null ? "" : v.toString(); }
        @Override public Long fromString(String s) {
            if (s == null || s.trim().isEmpty()) return null;
            s = s.replace(".", "").replace(",", "");
            return Long.parseLong(s);
        }
    }
}
