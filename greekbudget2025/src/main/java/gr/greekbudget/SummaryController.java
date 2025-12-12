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
import java.net.URI;
import java.util.Map;

public class SummaryController {

    // ================= UI =================
    @FXML private ComboBox<Integer> yearCombo;
    @FXML private ComboBox<String> ministryCombo;
    @FXML private VBox resultsBox;

    @FXML private Label linkTextLabel;
    @FXML private Hyperlink pdfLink;

    // ================= PDF TEXTS =================
    private final Map<Integer, String> pdfTexts = Map.of(
            2026, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2026, πατήστε ",
            2025, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2025, πατήστε ",
            2024, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2024, πατήστε ",
            2023, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2023, πατήστε ",
            2022, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2022, πατήστε "
    );

    // ================= PDF LINKS =================
    private final Map<Integer, String> pdfLinks = Map.of(
            2026, "https://minfin.gov.gr/wp-content/uploads/2025/11/Κρατικός-Προϋπολογισμός-2026.pdf",
            2025, "file:/mnt/data/Κρατικός-Προϋπολογισμός-2025_ΟΕ.pdf",
            2024, "file:/mnt/data/ΚΡΑΤΙΚΟΣ-ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ-2024.pdf",
            2023, "file:/mnt/data/21-11-2022-ΚΡΑΤΙΚΟΣ-ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ-2023.pdf",
            2022, "file:/mnt/data/ΚΡΑΤΙΚΟΣ-ΠΡΟΥΠΟΛΟΓΙΣΜΟΣ_2022.pdf"
    );

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {

        // Έτη
        yearCombo.getItems().addAll(2026, 2025, 2024, 2023, 2022);
        yearCombo.getSelectionModel().select(Integer.valueOf(2026));
        yearCombo.setOnAction(e -> reloadMinistries());

        reloadMinistries();
    }

    // ================= LOAD DATA =================
    private void reloadMinistries() {

        ministryCombo.getItems().clear();
        resultsBox.getChildren().clear();

        int year = yearCombo.getValue();

        updatePdfSection(year);

        Map<String, Long> data = MinistryBudgetData.getTotalsForYear(year);
        if (data == null) {
            showNoDataMessage(year);
            return;
        }

        ministryCombo.getItems().add("Όλα τα υπουργεία");
        ministryCombo.getItems().addAll(data.keySet());
        ministryCombo.getSelectionModel().selectFirst();
        ministryCombo.setOnAction(e -> updateView());

        updateView();
    }

    // ================= UPDATE VIEW =================
    private void updateView() {

        resultsBox.getChildren().clear();

        int year = yearCombo.getValue();
        String selected = ministryCombo.getValue();

        Map<String, Long> data = MinistryBudgetData.getTotalsForYear(year);
        if (data == null || selected == null) return;

        if (selected.equals("Όλα τα υπουργεία")) {
            for (var entry : data.entrySet()) {
                addMinistryBlock(entry.getKey(), entry.getValue());
            }
        } else {
            Long total = MinistryBudgetData.getTotal(year, selected);
            if (total != null) {
                addMinistryBlock(selected, total);
            }
        }
    }

    // ================= UI HELPERS =================
    private void addMinistryBlock(String ministry, long total) {

        Label name = new Label(ministry);
        name.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Label sum = new Label("ΣΥΝΟΛΟ: " + String.format("%,d €", total));
        sum.setStyle("-fx-font-size: 22px; -fx-padding: 5 0 25 0;");

        resultsBox.getChildren().addAll(name, sum);
    }

    private void showNoDataMessage(int year) {

        Label msg = new Label(
                "Δεν υπάρχουν διαθέσιμα δεδομένα για το έτος " + year
        );
        msg.setStyle("-fx-font-size: 22px; -fx-font-style: italic;");

        resultsBox.getChildren().add(msg);
    }

    // ================= PDF SECTION =================
    private void updatePdfSection(int year) {

        if (!pdfTexts.containsKey(year)) {
            linkTextLabel.setText("");
            pdfLink.setVisible(false);
            return;
        }

        linkTextLabel.setText(pdfTexts.get(year));
        pdfLink.setText("εδώ");
        pdfLink.setVisible(true);

        String url = pdfLinks.get(year);
        pdfLink.setOnAction(e -> openPdf(url));
    }

    private void openPdf(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= BACK =================
    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainView.fxml"));
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/styles/app.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
