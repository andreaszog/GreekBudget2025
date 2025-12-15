package gr.greekbudget;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Arrays;
import java.util.List;

public class AnalysisController {

    @FXML private ComboBox<String> yearCombo;
    @FXML private VBox contentBox;

    @FXML
    public void initialize() {
        yearCombo.getItems().addAll("2026", "2025", "2024", "2023", "2022");
        yearCombo.getSelectionModel().select("2026");

        updateAnalysis();
        yearCombo.setOnAction(e -> updateAnalysis());
    }

    private void updateAnalysis() {
        contentBox.getChildren().clear();

        String year = yearCombo.getValue();
        Label title = new Label("Ανάλυση Κρατικού Προϋπολογισμού " + year);
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-padding: 0 0 40 0;");

        Label paragraph = new Label(getPlaceholderText(year));
        paragraph.setWrapText(true);
        paragraph.setStyle("-fx-font-size: 22px; -fx-line-spacing: 8px;");

        contentBox.getChildren().addAll(title, paragraph);
    }

    /**
     * This text is displayed UNTIL revenue data is added by your friend.
     */
    private String getPlaceholderText(String year) {
        return
            "Η παρούσα ανάλυση για το έτος " + year + " βασίζεται αποκλειστικά " +
            "στα διαθέσιμα δεδομένα δαπανών, καθώς τα επίσημα στοιχεία εσόδων δεν " +
            "έχουν ακόμη ενσωματωθεί στο σύστημα.\n\n" +

            "Μόλις προστεθούν τα έσοδα, η εφαρμογή θα πραγματοποιεί αυτόματα:\n" +
            "• Υπολογισμό της συνολικής δημοσιονομικής θέσης\n" +
            "• Έλεγχο αν ο προϋπολογισμός είναι ισοσκελισμένος, ελλειμματικός ή πλεονασματικός\n" +
            "• Παραγωγή διοικητικού συμπεράσματος και εισηγήσεων πολιτικής\n\n" +

            "Μέχρι τότε, η παρούσα ενότητα λειτουργεί ως προκαταρκτική παρουσίαση, " +
            "παρέχοντας το πλαίσιο πάνω στο οποίο θα στηριχθεί η πλήρης ανάλυση.";
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Κεντρικό Μενού");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR loading MainView.fxml");
        }
    }
}
