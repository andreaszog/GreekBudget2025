package gr.greekbudget;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditExpenseController {

    @FXML private Label titleLabel;
    @FXML private Label ministryLabel;
    @FXML private Label categoryLabel;
    @FXML private Label currentValueLabel;
    @FXML private TextField newValueField;

    private SummaryController summaryController;
    private String ministry;
    private String category;
    private long currentValue;

    /**
     * Καλείται από το SummaryController μετά το load() του FXML.
     */
    public void initData(SummaryController parent, String ministry, String category, Long currentValue) {
        this.summaryController = parent;
        this.ministry = ministry;
        this.category = category;
        this.currentValue = (currentValue == null ? 0L : currentValue);

        titleLabel.setText("Επεξεργασία δαπάνης");
        ministryLabel.setText(ministry);
        categoryLabel.setText(category);
        currentValueLabel.setText(String.format("%,d €", this.currentValue));
        newValueField.setPromptText("π.χ. 1500000");
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    @FXML
    private void onSave() {
        String input = newValueField.getText();
        if (input == null || input.trim().isEmpty()) {
            showError("Πρέπει να εισάγετε μια νέα τιμή.");
            return;
        }

        // Απομακρύνουμε τυχόν κόμματα, κενά ή σύμβολα
        String cleaned = input.replaceAll("[^0-9]", "");
        if (cleaned.isEmpty()) {
            showError("Η νέα τιμή πρέπει να περιέχει αριθμούς (π.χ. 1500000).");
            return;
        }

        try {
            long newValue = Long.parseLong(cleaned);
            // Κλήση στο parent controller
            summaryController.updateExpense(ministry, category, newValue);
            closeWindow();
        } catch (NumberFormatException e) {
            showError("Η νέα τιμή είναι πολύ μεγάλη ή μη έγκυρη.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Σφάλμα");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
