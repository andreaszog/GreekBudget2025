package gr.greekbudget;

import gr.greekbudget.BudgetData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class IncomeExpenseController {

    @FXML
    private ComboBox<Integer> yearCombo;

    @FXML
    private VBox contentBox;

    @FXML
    public void initialize() {
        yearCombo.getItems().addAll(BudgetData.getRevenues().keySet());
        yearCombo.getSelectionModel().selectFirst();

        yearCombo.setOnAction(e -> updateView());
        updateView();
    }

    private void updateView() {
        contentBox.getChildren().removeIf(node -> node instanceof VBox);

        int year = yearCombo.getValue();

        Map<String, Long> revenues = BudgetData.getRevenues().get(year);
        Map<String, Long> expenses = BudgetData.getExpenses().get(year);

        VBox revenuesBox = createSection("ΕΣΟΔΑ", revenues);
        VBox expensesBox = createSection("ΕΞΟΔΑ", expenses);

        contentBox.getChildren().addAll(revenuesBox, expensesBox);
    }

    private VBox createSection(String title, Map<String, Long> data) {
        VBox box = new VBox(10);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        box.getChildren().add(titleLabel);

        long total = 0;

        for (var entry : data.entrySet()) {
            total += entry.getValue();

            Label line = new Label("• " + entry.getKey() + ": " +
                    String.format("%,d €", entry.getValue()));
            line.setStyle("-fx-font-size: 18px;");
            box.getChildren().add(line);
        }

        Label totalLabel = new Label("ΣΥΝΟΛΟ: " + String.format("%,d €", total));
        totalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        box.getChildren().add(totalLabel);
        return box;
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainView.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
