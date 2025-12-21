package gr.greekbudget;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ScenarioComparisonController {

    // ===== TABLE =====
    @FXML private TableView<ScenarioComparisonRow> table;
    @FXML private TableColumn<ScenarioComparisonRow, Number> yearCol;
    @FXML private TableColumn<ScenarioComparisonRow, String> ministryCol;
    @FXML private TableColumn<ScenarioComparisonRow, Number> oldCol;
    @FXML private TableColumn<ScenarioComparisonRow, Number> newCol;
    @FXML private TableColumn<ScenarioComparisonRow, Number> deltaCol;
    @FXML private TableColumn<ScenarioComparisonRow, Number> deltaPctCol;

    // ===== BOTTOM SUMMARY =====
    @FXML private VBox budgetImpactBox;

    private final ObservableList<ScenarioComparisonRow> rows =
            FXCollections.observableArrayList();

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
                setText(empty || v == null
                        ? null
                        : String.format("%.2f%%", v.doubleValue()));
            }
        });
    }

    // ================= DATA FROM PREVIOUS SCREEN =================

    public void setData(
            List<ScenarioComparisonRow> comparisonRows,
            List<BudgetImpactRow> impacts
    ) {
        // ----- table -----
        rows.setAll(comparisonRows);

        // ----- bottom summary -----
        budgetImpactBox.getChildren().clear();

        for (BudgetImpactRow r : impacts) {

            Label yearLbl = new Label("ΕΤΟΣ " + r.getYear());
            yearLbl.setStyle("-fx-text-fill:white; -fx-font-weight:bold;");

            Label beforeLbl = new Label(
                    "ΑΠΟΤΕΛΕΣΜΑ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ΠΡΙΝ ΤΙΣ ΤΡΟΠΟΠΟΙΗΣΕΙΣ: " + MoneyUtil.formatSignedEuro(r.getBefore())
            );
            beforeLbl.setStyle("-fx-text-fill:white;");

            Label afterLbl = new Label(
                    "ΑΠΟΤΕΛΕΣΜΑ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ΜΕΤΑ ΤΙΣ ΤΡΟΠΟΠΟΙΗΣΕΙΣ: " + MoneyUtil.formatSignedEuro(r.getAfter())
            );
            afterLbl.setStyle("-fx-text-fill:white;");

            Label diffLbl = new Label(
                    "ΑΠΟΤΕΛΕΣΜΑ ΚΡΑΤΙΚΟΥ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ (ΕΣΟΔΑ - ΕΞΟΔΑ): " +
                    MoneyUtil.formatSignedEuro(r.getAfter() - r.getBefore())
            );
            diffLbl.setStyle("-fx-text-fill:white;");

            budgetImpactBox.getChildren().addAll(
                    yearLbl,
                    beforeLbl,
                    afterLbl,
                    diffLbl,
                    new Separator()
            );
        }
    }

    // ================= BACK =================

    @FXML
    private void goBack(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/MinistryScenariosView.fxml")
            );

            Stage stage = (Stage) ((Node) e.getSource())
                    .getScene()
                    .getWindow();

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
                setText(empty || v == null
                        ? null
                        : MoneyUtil.formatEuro(v.longValue()));
            }
        };
    }

    private TableCell<ScenarioComparisonRow, Number> moneyCellSigned() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Number v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null
                        ? null
                        : MoneyUtil.formatSignedEuro(v.longValue()));
            }
        };
    }
}
