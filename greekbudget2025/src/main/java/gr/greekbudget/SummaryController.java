package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public class SummaryController {

        private String username;

        public void setUsername(String username) {
            this.username = username;
        }

    @FXML
    private ComboBox<String> yearCombo;

    @FXML
    private ComboBox<String> ministryCombo;

    @FXML
    private VBox resultsBox;

    @FXML
    private Label linkTextLabel;

    @FXML
    private Hyperlink pdfLink;

    private Map<String, Map<String, Long>> ministryExpenses;

    @FXML
    public void initialize() {
        loadBudgetData();

        yearCombo.getItems().addAll("2026", "2025", "2024", "2023", "2022");
        yearCombo.getSelectionModel().select("2026");

        ministryCombo.getItems().add("Όλα τα υπουργεία");
        ministryCombo.getItems().addAll(ministryExpenses.keySet());
        ministryCombo.getSelectionModel().select("Όλα τα υπουργεία");

        ministryCombo.setOnAction(e -> updateView());
        updateView();

        if (linkTextLabel != null && pdfLink != null) {
            linkTextLabel.setText("Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του έτους 2026, πατήστε ");
            pdfLink.setText("εδώ");
            pdfLink.setOnAction(e -> openPdf());
        }
    }

   @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent root = loader.load();

            // ΠΑΙΡΝΟΥΜΕ τον controller του MainView
            MainController mainController = loader.getController();

            // ΠΕΡΝΑΜΕ το username ξανά
            // (αν το κρατάς κάπου στο SummaryController)
            if (this.username != null) {
                mainController.setUsername(this.username);
            }

            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Δεν μπορώ να φορτώσω το MainView.fxml");
        }
    }



    private void openPdf() {
        String url = "https://minfin.gov.gr/wp-content/uploads/2025/11/Κρατικός-Προϋπολογισμός-2026.pdf";
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBudgetData() {
        ministryExpenses = new LinkedHashMap<>();

        ministryExpenses.put("Υπουργείο Εσωτερικών", Map.of(
                "Παροχές σε εργαζομένους", 60000000L,
                "Μεταβιβάσεις", 3400000000L,
                "Αγορές αγαθών και υπηρεσιών", 40000000L,
                "Πάγια περιουσιακά στοιχεία", 432000000L
        ));

        ministryExpenses.put("Υπουργείο Εξωτερικών", Map.of(
                "Παροχές σε εργαζομένους", 200000000L,
                "Μεταβιβάσεις", 50000000L,
                "Αγορές αγαθών και υπηρεσιών", 200000000L,
                "Πάγια περιουσιακά στοιχεία", 30000000L
        ));

        ministryExpenses.put("Υπουργείο Εθνικής Άμυνας", Map.of(
                "Παροχές σε εργαζομένους", 2500000000L,
                "Μεταβιβάσεις", 1000000000L,
                "Αγορές αγαθών και υπηρεσιών", 500000000L,
                "Εξοπλισμοί", 3200000000L
        ));

        ministryExpenses.put("Υπουργείο Υγείας", Map.of(
                "Παροχές σε εργαζομένους", 50000000L,
                "Μεταβιβάσεις", 7000000000L,
                "Αγορές αγαθών και υπηρεσιών", 150000000L,
                "Πάγια περιουσιακά στοιχεία", 1000000000L
        ));

        ministryExpenses.put("Υπουργείο Παιδείας", Map.of(
                "Παροχές σε εργαζομένους", 4200000000L,
                "Μεταβιβάσεις", 1932000000L,
                "Αγορές αγαθών και υπηρεσιών", 100000000L,
                "Πάγια περιουσιακά στοιχεία", 645000000L
        ));

        ministryExpenses.put("Υπουργείο Πολιτισμού", Map.of(
                "Παροχές σε εργαζομένους", 80000000L,
                "Μεταβιβάσεις", 220000000L,
                "Αγορές αγαθών και υπηρεσιών", 50000000L,
                "Πάγια περιουσιακά στοιχεία", 150000000L
        ));

        ministryExpenses.put("Υπουργείο Δικαιοσύνης", Map.of(
                "Παροχές σε εργαζομένους", 150000000L,
                "Μεταβιβάσεις", 0L,
                "Αγορές αγαθών και υπηρεσιών", 50000000L,
                "Πάγια περιουσιακά στοιχεία", 1000000000L
        ));

        ministryExpenses.put("Υπουργείο Κλιματικής Κρίσης και Πολιτικής Προστασίας", Map.of(
                "Παροχές σε εργαζομένους", 300000000L,
                "Αγορές αγαθών και υπηρεσιών", 100000000L,
                "Λοιπές δαπάνες", 438000000L,
                "Πάγια περιουσιακά στοιχεία", 600000000L
        ));

        ministryExpenses.put("Υπουργείο Προστασίας του Πολίτη", Map.of(
                "Παροχές σε εργαζομένους", 1800000000L,
                "Μεταβιβάσεις", 0L,
                "Αγορές αγαθών και υπηρεσιών", 400000000L,
                "Λοιπές δαπάνες", 210000000L,
                "Πάγια περιουσιακά στοιχεία", 100000000L
        ));

        ministryExpenses.put("Υπουργείο Εργασίας & Κοινωνικής Ασφάλισης", Map.of(
                "Παροχές σε εργαζομένους", 100000000L,
                "Κοινωνικές παροχές", 2200000000L,
                "Μεταβιβάσεις", 17000000000L,
                "Αγορές αγαθών και υπηρεσιών", 50000000L,
                "Πάγια περιουσιακά στοιχεία", 150000000L
        ));

        ministryExpenses.put("Υπουργείο Κοινωνικής Συνοχής & Οικογένειας", Map.of(
                "Παροχές σε εργαζομένους", 20000000L,
                "Κοινωνικές παροχές", 1500000000L,
                "Μεταβιβάσεις", 300000000L,
                "Αγορές αγαθών και υπηρεσιών", 10000000L,
                "Πάγια περιουσιακά στοιχεία", 50000000L
        ));

        ministryExpenses.put("Υπουργείο Υποδομών & Μεταφορών", Map.of(
                "Παροχές σε εργαζομένους", 200000000L,
                "Μεταβιβάσεις", 800000000L,
                "Αγορές αγαθών και υπηρεσιών", 30000000L,
                "Πάγια περιουσιακά στοιχεία", 2360000000L
        ));

        ministryExpenses.put("Υπουργείο Ναυτιλίας & Νησιωτικής Πολιτικής", Map.of(
                "Παροχές σε εργαζομένους", 80000000L,
                "Μεταβιβάσεις", 268000000L,
                "Αγορές αγαθών και υπηρεσιών", 20000000L,
                "Πάγια περιουσιακά στοιχεία", 389000000L
        ));

        ministryExpenses.put("Υπουργείο Ψηφιακής Διακυβέρνησης", Map.of(
                "Παροχές σε εργαζομένους", 50000000L,
                "Μεταβιβάσεις", 20000000L,
                "Αγορές αγαθών και υπηρεσιών", 30000000L,
                "Πάγια περιουσιακά στοιχεία", 188000000L
        ));
    }

  private void updateView() {

    resultsBox.getChildren().clear();

    String selected = ministryCombo.getValue();
    if (selected == null) return;

    if (selected.equals("Όλα τα υπουργεία")) {

        long grandTotal = 0L;

        for (var ministryEntry : ministryExpenses.entrySet()) {

            String ministryName = ministryEntry.getKey();
            Map<String, Long> expenses = ministryEntry.getValue();

            Label ministryLabel = new Label(ministryName);
            ministryLabel.setStyle(
                    "-fx-font-size: 28px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 25 0 15 0;"
            );
            ministryLabel.setWrapText(true);
            ministryLabel.setMaxWidth(Double.MAX_VALUE);

            resultsBox.getChildren().add(ministryLabel);

            long ministryTotal = 0L;

            for (var entry : expenses.entrySet()) {

                String category = entry.getKey();
                Long amount = entry.getValue();

                ministryTotal += amount;
                grandTotal += amount;

                Label line = new Label("• " + category + ": " + String.format("%,d €", amount));
                line.setStyle(
                        "-fx-font-size: 20px;" +
                        "-fx-padding: 8 0 8 15;"
                );

                resultsBox.getChildren().add(line);
            }

            Label ministrySum = new Label("ΣΥΝΟΛΟ : " + String.format("%,d €", ministryTotal));
            ministrySum.setStyle(
                    "-fx-font-size: 22px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 15 0 25 0;"
            );

            resultsBox.getChildren().add(ministrySum);
        }

        Label allTotal = new Label("ΣΥΝΟΛΟ ΟΛΩΝ ΤΩΝ ΥΠΟΥΡΓΕΙΩΝ: " +
                String.format("%,d €", grandTotal));
        allTotal.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 30 0 0 0;"
        );

        resultsBox.getChildren().add(allTotal);

        return;
    }

    Map<String, Long> expenses = ministryExpenses.get(selected);

    Label title = new Label(selected);
    title.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 20 0 20 0;"
    );
    title.setWrapText(true);
    title.setMaxWidth(Double.MAX_VALUE);

    resultsBox.getChildren().add(title);

    long total = 0L;

    for (var entry : expenses.entrySet()) {
        String category = entry.getKey();
        Long amount = entry.getValue();

        total += amount;

        Label line = new Label("• " + category + ": " + String.format("%,d €", amount));
        line.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-padding: 12 0 12 15;"
        );

        resultsBox.getChildren().add(line);
    }

    Label totalLabel = new Label("ΣΥΝΟΛΟ : " + String.format("%,d €", total));
    totalLabel.setStyle(
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 25 0 0 0;"
    );

    resultsBox.getChildren().add(totalLabel);
    }
}

