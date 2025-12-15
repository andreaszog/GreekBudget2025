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

    // ================= PDF TEXT =================
    private final Map<Integer, String> pdfTexts = Map.of(
            2026, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2026, πατήστε ",
            2025, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2025, πατήστε ",
            2024, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2024, πατήστε ",
            2023, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2023, πατήστε ",
            2022, "Για να δείτε τον πλήρη Κρατικό Προϋπολογισμό του οικονομικού έτους 2022, πατήστε "
    );

    // ================= PDF LINKS (ΜΟΝΟ URLs) =================
    private final Map<Integer, String> pdfLinks = Map.of(
            2026, "https://minfin.gov.gr/wp-content/uploads/2025/11/%CE%9A%CF%81%CE%B1%CF%84%CE%B9%CE%BA%CF%8C%CF%82-%CE%A0%CF%81%CE%BF%CF%8B%CF%80%CE%BF%CE%BB%CE%BF%CE%B3%CE%B9%CF%83%CE%BC%CF%8C%CF%82-2026.pdf",
            2025, "https://minfin.gov.gr/wp-content/uploads/2024/11/%CE%9A%CF%81%CE%B1%CF%84%CE%B9%CE%BA%CF%8C%CF%82-%CE%A0%CF%81%CE%BF%CF%8B%CF%80%CE%BF%CE%BB%CE%BF%CE%B3%CE%B9%CF%83%CE%BC%CF%8C%CF%82-2025_%CE%9F%CE%95.pdf",
            2024, "https://minfin.gov.gr/wp-content/uploads/2023/11/%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2024.pdf",
            2023, "https://minfin.gov.gr/wp-content/uploads/2023/11/21-11-2022-%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%AB%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2023.pdf",
            2022, "https://minfin.gov.gr/wp-content/uploads/2023/11/%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3_2022.pdf"
    );

    // ================= INIT =================
    @FXML
    public void initialize() {

        yearCombo.getItems().addAll(2026, 2025, 2024, 2023, 2022);
        yearCombo.getSelectionModel().select(Integer.valueOf(2026));
        yearCombo.setOnAction(e -> reloadView());

        reloadView();
    }

    // ================= LOAD VIEW =================
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

    // ================= UPDATE RESULTS =================
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

    // ================= UI HELPERS =================
    private void addMinistryBlock(String ministry, long total) {

        Label name = new Label(ministry);
        name.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        Label sum = new Label("ΣΥΝΟΛΟ: " + String.format("%,d €", total));
        sum.setStyle("-fx-font-size: 22px; -fx-padding: 5 0 25 0;");

        resultsBox.getChildren().addAll(name, sum);
    }

    // ================= PDF =================
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
