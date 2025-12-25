package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

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

    // Βάλε τα δικά σου URLs (τα αφήνω όπως είχες πριν αν θες)
    private final Map<Integer, String> pdfLinks = Map.of(
            2026, "https://minfin.gov.gr/wp-content/uploads/2025/11/%CE%9A%CF%81%CE%B1%CF%84%CE%B9%CE%BA%CF%8C%CF%82-%CE%A0%CF%81%CE%BF%CF%8B%CF%80%CE%BF%CE%BB%CE%BF%CE%B3%CE%B9%CF%83%CE%BC%CF%8C%CF%82-2026.pdf",
            2025, "https://minfin.gov.gr/wp-content/uploads/2024/11/%CE%9A%CF%81%CE%B1%CF%84%CE%B9%CE%BA%CF%8C%CF%82-%CE%A0%CF%81%CE%BF%CF%8B%CF%80%CE%BF%CE%BB%CE%BF%CE%B3%CE%B9%CF%83%CE%BC%CF%8C%CF%82-2025_%CE%9F%CE%95.pdf",
            2024, "https://minfin.gov.gr/wp-content/uploads/2023/11/%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2024.pdf",
            2023, "https://minfin.gov.gr/wp-content/uploads/2023/11/21-11-2022-%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%87%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3-2023.pdf",
            2022, "https://minfin.gov.gr/wp-content/uploads/2023/11/%CE%9A%CE%A1%CE%91%CE%A4%CE%99%CE%9A%CE%9F%CE%A3-%CE%A0%CE%A1%CE%9F%CE%A5%CE%A0%CE%9F%CE%9B%CE%9F%CE%93%CE%99%CE%A3%CE%9C%CE%9F%CE%A3_2022.pdf"
    );

    // ================= INTERNAL STATE =================
    private static final String ALL = "Όλα τα υπουργεία";

    // τι έχει διαλέξει ο χρήστης (σε canonical μορφή, ώστε να επιβιώνει όταν αλλάζει όνομα ανά έτος)
    private String selectedCanonical = null;

    // display -> canonical mapping για το τρέχον έτος
    private final Map<String, String> displayToCanonical = new LinkedHashMap<>();

    // canonical -> ποσό για το τρέχον έτος (συνοψισμένο)
    private Map<String, Long> currentCanonicalTotals = new LinkedHashMap<>();

    @FXML
    public void initialize() {

        yearCombo.getItems().addAll(2026, 2025, 2024, 2023, 2022);
        yearCombo.getSelectionModel().select(Integer.valueOf(2026));

        yearCombo.setOnAction(e -> reloadKeepSelection());

        ministryCombo.setOnAction(e -> {
            // κράτα επιλογή σε canonical (όχι σε display)
            String display = ministryCombo.getValue();
            if (display == null || display.equals(ALL)) {
                selectedCanonical = null;
            } else {
                selectedCanonical = displayToCanonical.getOrDefault(display, null);
            }
            updateResults();
        });

        reloadKeepSelection();
    }

    // ================= LOAD / KEEP SELECTION =================
    private void reloadKeepSelection() {

        resultsBox.getChildren().clear();

        int year = yearCombo.getValue();
        updatePdfSection(year);

        // 1) φορτώνουμε raw data
        Map<String, Long> raw = MinistryBudgetData.getTotalsForYear(year);
        if (raw == null) return;

        // 2) κάνουμε canonical aggregation (για να μην χάνεται επιλογή όταν αλλάζει όνομα ανά έτος)
        Map<String, Long> canonicalTotals = new LinkedHashMap<>();
        for (var e : raw.entrySet()) {
            String canonical = MinistryNameNormalizer.canonical(e.getKey());
            canonicalTotals.merge(canonical, e.getValue(), Long::sum);
        }
        currentCanonicalTotals = canonicalTotals;

        // 3) γεμίζουμε combo (display labels)
        displayToCanonical.clear();
        ministryCombo.getItems().clear();
        ministryCombo.getItems().add(ALL);

        List<String> canonicals = new ArrayList<>(canonicalTotals.keySet());
        canonicals.sort(String::compareTo);

        for (String c : canonicals) {
            String display = MinistryNameNormalizer.displayLabel(c);
            // αν τυχόν δύο canonical δώσουν ίδιο display, κάνε το μοναδικό
            String uniqueDisplay = display;
            int i = 2;
            while (displayToCanonical.containsKey(uniqueDisplay)) {
                uniqueDisplay = display + " (" + i + ")";
                i++;
            }
            displayToCanonical.put(uniqueDisplay, c);
            ministryCombo.getItems().add(uniqueDisplay);
        }

        // 4) ΕΠΑΝΑΦΟΡΑ επιλογής (ΔΕΝ γυρνάει αυτόματα σε ALL αν υπάρχει στο νέο έτος)
        if (selectedCanonical != null) {
            String displayMatch = displayToCanonical.entrySet().stream()
                    .filter(en -> en.getValue().equals(selectedCanonical))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

            if (displayMatch != null) {
                ministryCombo.setValue(displayMatch);
            } else {
                // αν ΔΕΝ υπάρχει στο νέο έτος, μόνο τότε πάμε All
                ministryCombo.setValue(ALL);
                selectedCanonical = null;
            }
        } else {
            // αν δεν είχε επιλέξει τίποτα, κράτα το current value αν είναι valid αλλιώς All
            String cur = ministryCombo.getValue();
            if (cur == null || !ministryCombo.getItems().contains(cur)) {
                ministryCombo.setValue(ALL);
            }
        }

        updateResults();
    }

    // ================= RESULTS =================
    private void updateResults() {

        resultsBox.getChildren().clear();

        int year = yearCombo.getValue();
        if (currentCanonicalTotals == null || currentCanonicalTotals.isEmpty()) return;

        // sorted by amount desc
        List<Map.Entry<String, Long>> sorted = currentCanonicalTotals.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());

        int totalMinistries = sorted.size();
        long totalBudget = sorted.stream().mapToLong(Map.Entry::getValue).sum();
        double avg = totalBudget / (double) totalMinistries;

        String selectedDisplay = ministryCombo.getValue();
        String selectedCanonicalLocal = null;

        if (selectedDisplay != null && !selectedDisplay.equals(ALL)) {
            selectedCanonicalLocal = displayToCanonical.get(selectedDisplay);
        }

        if (selectedCanonicalLocal == null) {
            // ALL
            int rank = 1;
            for (var e : sorted) {
                addMinistryBlock(year, e.getKey(), e.getValue(), rank, totalMinistries, totalBudget, avg);
                rank++;
            }
        } else {
            // SINGLE: κρατάμε ΤΟ ΣΥΝΟΛΙΚΟ rank του υπουργείου στο έτος
            int rank = 1;
            Map.Entry<String, Long> target = null;
            for (var e : sorted) {
                if (e.getKey().equals(selectedCanonicalLocal)) {
                    target = e;
                    break;
                }
                rank++;
            }
            if (target != null) {
                addMinistryBlock(year, target.getKey(), target.getValue(), rank, totalMinistries, totalBudget, avg);
            }
        }
    }

    // ================= UI BLOCK =================
    private void addMinistryBlock(
            int year,
            String canonicalMinistry,
            long amount,
            int rank,
            int totalMinistries,
            long totalBudget,
            double avg
    ) {
        String displayName = MinistryNameNormalizer.displayLabel(canonicalMinistry);

        double pctOfTotal = totalBudget == 0 ? 0 : (amount * 100.0 / totalBudget);
        long avgRounded = Math.round(avg);
        long diffEuro = amount - avgRounded;
        double diffPct = (avgRounded == 0) ? 0 : (diffEuro * 100.0 / avgRounded);

        // πιο “ανθρώπινα” labels
        String diffText;
        if (diffEuro > 0) {
            diffText = String.format("Πάνω από τον μέσο όρο κατά %,d € (%.1f%%).", diffEuro, diffPct);
        } else if (diffEuro < 0) {
            diffText = String.format("Κάτω από τον μέσο όρο κατά %,d € (%.1f%%).", Math.abs(diffEuro), Math.abs(diffPct));
        } else {
            diffText = "Ίσο με τον μέσο όρο δαπανών.";
        }

        String level;
        if (pctOfTotal < 5) level = "🟢 Χαμηλή βαρύτητα";
        else if (pctOfTotal <= 12) level = "🟡 Μεσαία βαρύτητα";
        else level = "🔴 Υψηλή βαρύτητα";

        // TITLE: RANKING ΜΕ #
        Label title = new Label("#" + rank + "  " + displayName);
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label sub = new Label("Κατάταξη δαπανών για το έτος " + year + " (σύνολο " + totalMinistries + " υπουργεία)");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

        Label amountLbl = new Label("Σύνολο δαπάνης: " + String.format("%,d €", amount));
        amountLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label pctLbl = new Label(String.format("Μερίδιο στο σύνολο: %.2f%%", pctOfTotal));
        pctLbl.setStyle("-fx-font-size: 14px;");

        // ΜΕΣΟΣ ΟΡΟΣ: ΤΙ ΑΚΡΙΒΩΣ ΕΙΝΑΙ
        Label avgExplain = new Label(
                "Μέσος όρος δαπάνης ανά υπουργείο για το έτος " + year + ": " + String.format("%,d €", avgRounded)
        );
        avgExplain.setStyle("-fx-font-size: 14px;");

        Label avgDiff = new Label(diffText);
        avgDiff.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        Label levelLbl = new Label(level);
        levelLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        VBox box = new VBox(6, title, sub, amountLbl, pctLbl, avgExplain, avgDiff, levelLbl);
        box.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 12;
                -fx-padding: 14;
                -fx-border-color: #e2e2e2;
                -fx-border-radius: 12;
                """);

        resultsBox.getChildren().add(box);
    }

    // ================= PDF =================
    private void updatePdfSection(int year) {
        linkTextLabel.setText(pdfTexts.getOrDefault(year, ""));
        pdfLink.setText("εδώ");
        String url = pdfLinks.get(year);
        pdfLink.setOnAction(e -> openPdf(url));
    }

    private void openPdf(String url) {
        try {
            if (url != null && !url.isBlank()) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception ignored) {}
    }

    // ================= BACK =================
    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainView.fxml"));

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.getScene().setRoot(root);
            stage.setTitle("Dashboard");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
