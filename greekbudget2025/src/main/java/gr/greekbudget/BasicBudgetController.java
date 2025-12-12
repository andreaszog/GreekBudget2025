package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class BasicBudgetController {

    @FXML
    private VBox contentBox;

    // ΠΡΟΣΩΡΙΝΑ: δουλεύουμε μόνο με 2026
    private static final int YEAR = 2026;

    @FXML
    public void initialize() {
        showRevenues();
        showExpenses();
    }

    private void showRevenues() {
        Label title = new Label("Έσοδα Κρατικού Προϋπολογισμού " + YEAR);
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        contentBox.getChildren().add(title);

        Map<String, Long> revenues =
                BudgetData.getRevenues().get(YEAR);

        if (revenues == null) {
            contentBox.getChildren().add(new Label("Δεν βρέθηκαν έσοδα."));
            return;
        }

        for (Map.Entry<String, Long> entry : revenues.entrySet()) {
            Label line = new Label("• " + entry.getKey() + ": " +
                    String.format("%,d €", entry.getValue()));
            line.setStyle("-fx-font-size: 18px;");
            contentBox.getChildren().add(line);
        }

        Label total = new Label("ΣΥΝΟΛΟ ΕΣΟΔΩΝ: " +
                String.format("%,d €", BudgetData.getTotalRevenues(YEAR)));
        total.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        contentBox.getChildren().add(total);
    }

    private void showExpenses() {
        Label title = new Label("Έξοδα Κρατικού Προϋπολογισμού " + YEAR);
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 25 0 0 0;");
        contentBox.getChildren().add(title);

        Map<String, Long> expenses =
                BudgetData.getExpenses().get(YEAR);

        if (expenses == null) {
            contentBox.getChildren().add(new Label("Δεν βρέθηκαν έξοδα."));
            return;
        }

        for (Map.Entry<String, Long> entry : expenses.entrySet()) {
            Label line = new Label("• " + entry.getKey() + ": " +
                    String.format("%,d €", entry.getValue()));
            line.setStyle("-fx-font-size: 18px;");
            contentBox.getChildren().add(line);
        }

        Label total = new Label("ΣΥΝΟΛΟ ΕΞΟΔΩΝ: " +
                String.format("%,d €", BudgetData.getTotalExpenses(YEAR)));
        total.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        contentBox.getChildren().add(total);
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
