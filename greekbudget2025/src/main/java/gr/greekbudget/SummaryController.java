package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;
import java.util.Map;

public class SummaryController {

    @FXML private ComboBox<Integer> yearCombo;
    @FXML private ComboBox<String> ministryCombo;
    @FXML private VBox resultsBox;

    @FXML private Label linkTextLabel;
    @FXML private Hyperlink pdfLink;

    private final Map<Integer, String> pdfTexts = Map.of(
            2026, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2026, πατήστε ",
            2025, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2025, πατήστε ",
            2024, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2024, πατήστε ",
            2023, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2023, πατήστε ",
            2022, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2022, πατήστε "
    );

    private final Map<Integer, String> pdfLinks = Map.of(
            2026, "https://minfin.gov.gr/",
            2025, "https://minfin.gov.gr/",
            2024, "https://minfin.gov.gr/",
            2023, "https://minfin.gov.gr/",
            2022, "https://minfin.gov.gr/"
    );

    @FXML
    public void initialize() {
        yearCombo.getItems().addAll(2026, 2025, 2024, 2023, 2022);
        yearCombo.getSelectionModel().select(Integer.valueOf(2026));
        yearCombo.setOnAction(e -> reloadView());
        reloadView();
    }

    private void reloadView() {
        resultsBox.getChildren().clear();
        ministryCombo.getItems().clear();

        int year = yearCombo.getValue();
        updatePdfSection(year);

        Map<String, Long> data = MinistryBudgetData.getTotalsForYear(year);
        if (data == null) return;

        ministryCombo.getItems().add("Όλα τα υπουργεία");
        ministryCombo.getItems().addAll(data.keySet());
        ministryCombo.getSelectionModel().selectFirst();

        ministryCombo.setOnAction(e -> updateResults());
        updateResults();
    }

    private void updateResults() {
        resultsBox.getChildren().clear();

        int year = yearCombo.getValue();
        String selected = ministryCombo.getValue();

        Map<String, Long> data = MinistryBudgetData.getTotalsForYear(year);
        if (data == null || selected == null) return;

        if (selected.equals("Όλα τα υπουργεία")) {
            data.forEach(this::addMinistryBlock);
        } else {
            addMinistryBlock(selected, data.get(selected));
        }
    }

    private void addMinistryBlock(String ministry, long total) {
        Label name = new Label(ministry);
        name.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Label sum = new Label("ΣΥΝΟΛΟ: " + String.format("%,d €", total));
        sum.setStyle("-fx-font-size: 22px; -fx-padding: 5 0 25 0;");

        resultsBox.getChildren().addAll(name, sum);
    }

    private void updatePdfSection(int year) {
        linkTextLabel.setText(pdfTexts.get(year));
        pdfLink.setText("εδώ");

        String url = pdfLinks.get(year);
        pdfLink.setOnAction(e -> openPdf(url));
    }

    private void openPdf(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= BACK (FIXED)
    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainView.fxml"));

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.getScene().setRoot(root);   // ✅ ΟΧΙ new Stage
            stage.setTitle("Dashboard");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
