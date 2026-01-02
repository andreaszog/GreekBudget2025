package gr.greekbudget;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class IncomeExpenseController {

    @FXML
    private ComboBox<Integer> yearComboBox;

    @FXML
    private TableView<CategoryAmount> revenueTable;

    @FXML
    private TableView<CategoryAmount> expenseTable;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label totalExpenseLabel;

    @FXML
    private Button editValuesButton;

    @FXML
    private Button resetValuesButton;

    private Map<Integer, Map<String, Long>> originalRevenues;
    private Map<Integer, Map<String, Long>> originalExpenses;

    @FXML
    public void initialize() {
        originalRevenues = new LinkedHashMap<>();
        originalExpenses = new LinkedHashMap<>();

        BudgetData.getRevenues().forEach((year, map) -> originalRevenues.put(year, new LinkedHashMap<>(map)));
        BudgetData.getExpenses().forEach((year, map) -> originalExpenses.put(year, new LinkedHashMap<>(map)));

        yearComboBox.getItems().addAll(BudgetData.getRevenues().keySet());
        yearComboBox.getSelectionModel().selectFirst();
        yearComboBox.setOnAction(e -> loadTables());

        setupTable(revenueTable);
        setupTable(expenseTable);

        loadTables();

        editValuesButton.setOnAction(e -> enableEditing(true));
        resetValuesButton.setOnAction(e -> resetValues());
    }

    private void setupTable(TableView<CategoryAmount> table) {
        TableColumn<CategoryAmount, String> categoryCol = new TableColumn<>("Κατηγορία");
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        TableColumn<CategoryAmount, Long> amountCol = new TableColumn<>("Ποσό");
        amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        amountCol.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.LongStringConverter()));
        amountCol.setOnEditCommit(event -> {
            long newValue = event.getNewValue();
            if (newValue > 0) {
                event.getRowValue().setAmount(newValue);
                updateBudgetData(event.getRowValue(), table);
                updateTotals();
            } else {
                event.getRowValue().setAmount(event.getOldValue());
                table.refresh();
            }
        });

        table.getColumns().setAll(categoryCol, amountCol);
        table.setEditable(false);
    }

    private void loadTables() {
        int year = yearComboBox.getValue();

        revenueTable.setItems(toObservableList(BudgetData.getRevenues().get(year)));
        expenseTable.setItems(toObservableList(BudgetData.getExpenses().get(year)));

        updateTotals();
    }

    private ObservableList<CategoryAmount> toObservableList(Map<String, Long> map) {
        ObservableList<CategoryAmount> list = FXCollections.observableArrayList();
        map.forEach((k, v) -> list.add(new CategoryAmount(k, v)));
        return list;
    }

    private void enableEditing(boolean enable) {
        revenueTable.setEditable(enable);
        expenseTable.setEditable(enable);
    }

    private void updateBudgetData(CategoryAmount item, TableView<CategoryAmount> table) {
        int year = yearComboBox.getValue();
        if (table == revenueTable) {
            BudgetData.getRevenues().get(year).put(item.getCategory(), item.getAmount());
        } else {
            BudgetData.getExpenses().get(year).put(item.getCategory(), item.getAmount());
        }
    }

    private void updateTotals() {
        int year = yearComboBox.getValue();
        totalRevenueLabel.setText(String.valueOf(BudgetData.getTotalRevenues(year)));
        totalExpenseLabel.setText(String.valueOf(BudgetData.getTotalExpenses(year)));
    }

    private void resetValues() {
        int year = yearComboBox.getValue();
        BudgetData.getRevenues().put(year, new LinkedHashMap<>(originalRevenues.get(year)));
        BudgetData.getExpenses().put(year, new LinkedHashMap<>(originalExpenses.get(year)));

        loadTables();
    }

    // ===== Προσθήκη μόνο για το Back button =====
    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/MainView.fxml")
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.getScene().setRoot(root);   // ⬅ ΚΡΑΤΑΕΙ FULLSCREEN
            stage.setTitle("Dashboard");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    
