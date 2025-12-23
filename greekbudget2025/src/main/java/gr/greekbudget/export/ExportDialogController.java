package gr.greekbudget.export;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class ExportDialogController {

    // ===== BASIC CHARTS =====
    @FXML private CheckBox revenueCheck;
    @FXML private CheckBox expenseCheck;
    @FXML private CheckBox summaryCheck;
    @FXML private CheckBox pieAllCheck;
    @FXML private CheckBox pieNoFinanceCheck;

    // ===== STATE =====
    private ExportOptions result;
    private int year;

    // =========================================================
    // INIT
    // =========================================================
    @FXML
    public void initialize() {
        // τίποτα εδώ – απλό dialog
    }

    // =========================================================
    // CALLED BY ChartsController
    // =========================================================
    public void setYear(int year) {
        this.year = year;
    }

    public ExportOptions getResult() {
        return result;
    }

    // =========================================================
    // BUTTONS
    // =========================================================
    @FXML
    private void cancel() {
        result = null;
        close();
    }

    @FXML
    private void confirm() {

        // constructor με year (όπως έχεις στο ExportOptions)
        ExportOptions opt = new ExportOptions(year);

        opt.exportRevenueChart = revenueCheck.isSelected();
        opt.exportExpenseChart = expenseCheck.isSelected();
        opt.exportSummaryBars  = summaryCheck.isSelected();
        opt.exportPieAll       = pieAllCheck.isSelected();
        opt.exportPieNoFinance = pieNoFinanceCheck.isSelected();

        // πρέπει να έχει τουλάχιστον 1 επιλογή
        boolean anySelected =
                opt.exportRevenueChart ||
                opt.exportExpenseChart ||
                opt.exportSummaryBars ||
                opt.exportPieAll ||
                opt.exportPieNoFinance;

        if (!anySelected) {
            new Alert(
                    Alert.AlertType.WARNING,
                    "Πρέπει να επιλέξεις τουλάχιστον 1 γράφημα για export."
            ).showAndWait();
            return;
        }

        result = opt;
        close();
    }

    // =========================================================
    // CLOSE
    // =========================================================
    private void close() {
        Stage stage = (Stage) revenueCheck.getScene().getWindow();
        stage.close();
    }
}
