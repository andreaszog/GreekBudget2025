package gr.greekbudget;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.*;

public class RevExpScenariosController {

    // ================= UI =================
    @FXML private ComboBox<Integer> yearBox;
    @FXML private Button compareBtn;

    @FXML private TableView<RevExpRow> table;
    @FXML private TableColumn<RevExpRow, Boolean> selectCol;
    @FXML private TableColumn<RevExpRow, String> typeCol;
    @FXML private TableColumn<RevExpRow, String> nameCol;
    @FXML private TableColumn<RevExpRow, Number> amountCol;

    @FXML private TableView<RevExpScenarioRow> selectedTable;
    @FXML private TableColumn<RevExpScenarioRow, Number> selYearCol;
    @FXML private TableColumn<RevExpScenarioRow, String> selTypeCol;
    @FXML private TableColumn<RevExpScenarioRow, String> selNameCol;
    @FXML private TableColumn<RevExpScenarioRow, Number> oldAmountCol;
    @FXML private TableColumn<RevExpScenarioRow, Long> newAmountCol;

    // ================= DATA =================
    private final ObservableList<RevExpRow> allRows =
            FXCollections.observableArrayList();

    private final ObservableList<RevExpScenarioRow> scenarioRows =
            FXCollections.observableArrayList();

    private final Map<String, RevExpScenarioRow> scenarioByKey =
            new HashMap<>();

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {

        compareBtn.setDisable(true);

        // years
        List<Integer> years = new ArrayList<>(BudgetData.getRevenues().keySet());
        years.sort(Comparator.reverseOrder());
        yearBox.setItems(FXCollections.observableArrayList(years));
        yearBox.setValue(years.get(0));
        yearBox.valueProperty().addListener((o,a,b) -> loadYear(b));

        // LEFT TABLE
        table.setItems(allRows);
        table.setEditable(true);

        selectCol.setCellValueFactory(c -> c.getValue().selectedProperty());
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));

        typeCol.setCellValueFactory(c -> c.getValue().typeProperty());
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        amountCol.setCellValueFactory(c -> c.getValue().amountProperty());

        // RIGHT TABLE
        selectedTable.setItems(scenarioRows);
        selectedTable.setEditable(true);

        selYearCol.setCellValueFactory(c -> c.getValue().yearProperty());
        selTypeCol.setCellValueFactory(c -> c.getValue().typeProperty());
        selNameCol.setCellValueFactory(c -> c.getValue().nameProperty());
        oldAmountCol.setCellValueFactory(c -> c.getValue().oldAmountProperty());
        newAmountCol.setCellValueFactory(c -> c.getValue().newAmountProperty());

        newAmountCol.setCellFactory(
                TextFieldTableCell.forTableColumn(new LongConverter())
        );

        newAmountCol.setOnEditCommit(e -> {
            e.getRowValue().setNewAmount(e.getNewValue());
            updateCompareButton();
        });

        loadYear(yearBox.getValue());
    }

    // ================= LOAD YEAR =================
    private void loadYear(int year) {

        allRows.clear();
        scenarioRows.clear();
        scenarioByKey.clear();
        updateCompareButton();

        BudgetData.getRevenues().getOrDefault(year, Map.of())
                .forEach((n,a) -> addLeftRow(year, "Έσοδα", n, a));

        BudgetData.getExpenses().getOrDefault(year, Map.of())
                .forEach((n,a) -> addLeftRow(year, "Έξοδα", n, a));
    }

    private void addLeftRow(int year, String type, String name, long amount) {

        RevExpRow r = new RevExpRow(year, type, name, amount);

        r.selectedProperty().addListener((o,oldV,newV) -> {
            if (newV) addScenario(r);
            else removeScenario(r);
            updateCompareButton();
        });

        allRows.add(r);
    }

    private void addScenario(RevExpRow r) {
        if (scenarioByKey.containsKey(r.key())) return;

        RevExpScenarioRow sr =
                new RevExpScenarioRow(
                        r.getYear(),
                        r.getType(),
                        r.getName(),
                        r.getAmount()
                );

        scenarioByKey.put(r.key(), sr);
        scenarioRows.add(sr);
    }

    private void removeScenario(RevExpRow r) {
        RevExpScenarioRow sr = scenarioByKey.remove(r.key());
        if (sr != null) scenarioRows.remove(sr);
    }

    // ================= BUTTON ENABLE LOGIC =================
    private void updateCompareButton() {

        if (scenarioRows.isEmpty()) {
            compareBtn.setDisable(true);
            return;
        }

        for (RevExpScenarioRow r : scenarioRows) {
            if (r.getNewAmount() == null) {
                compareBtn.setDisable(true);
                return;
            }
        }

        compareBtn.setDisable(false);
    }

    // ================= COMPARE =================
    @FXML
    private void compare(ActionEvent e) {

        List<RevExpScenarioComparisonRow> comparison =
                new ArrayList<>();

        for (RevExpScenarioRow r : scenarioRows) {
            comparison.add(
                    new RevExpScenarioComparisonRow(
                            r.getYear(),
                            r.getType(),
                            r.getName(),
                            r.getOldAmount(),
                            r.getNewAmount()
                    )
            );
        }

        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(
                            "/RevExpScenarioComparisonView.fxml"));

            Parent root = loader.load();

            RevExpScenarioComparisonController ctrl =
                    loader.getController();

            ctrl.setData(comparison);

            Stage stage =
                    (Stage) ((Node) e.getSource())
                            .getScene()
                            .getWindow();

            stage.getScene().setRoot(root);
            stage.setTitle("Σύγκριση Σεναρίου Εσόδων - Εξόδων");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ================= BACK =================
    @FXML
    private void goBack(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/ScenariosView.fxml"));
            Stage stage = (Stage)((Node)e.getSource())
                    .getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Σενάρια");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ================= CONVERTER =================
    private static class LongConverter extends StringConverter<Long> {
        @Override public String toString(Long v) {
            return v == null ? "" : v.toString();
        }
        @Override public Long fromString(String s) {
            if (s == null || s.isBlank()) return null;
            return Long.parseLong(s.replace(".","").replace(",",""));
        }
    }
}
