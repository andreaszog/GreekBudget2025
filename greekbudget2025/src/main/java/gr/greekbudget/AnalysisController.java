package gr.greekbudget;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class AnalysisController {

    @FXML private ComboBox<String> yearCombo;
    @FXML private VBox contentBox;

    @FXML
    public void initialize() {
        yearCombo.getItems().addAll("2026", "2025", "2024", "2023", "2022");
        yearCombo.getSelectionModel().select("2026");
        yearCombo.setOnAction(e -> updateAnalysis());
        updateAnalysis();
    }

    private void updateAnalysis() {

        contentBox.getChildren().clear();

        int year = Integer.parseInt(yearCombo.getValue());

        long revenues = BudgetData.getTotalRevenues(year);
        long expenses = BudgetData.getTotalExpenses(year);

        long balance = revenues - expenses;

        long interest = BudgetData.getExpenses()
                .get(year)
                .getOrDefault("Τόκοι", 0L);

        long primaryBalance = balance + interest;

        long investments = BudgetData.getExpenses()
                .get(year)
                .getOrDefault("Πάγια περιουσιακά στοιχεία", 0L);

        double balancePct = percent(balance, revenues);
        double primaryPct = percent(primaryBalance, revenues);
        double interestPct = percent(interest, revenues);
        double investPct = percent(investments, revenues);

        addTitle("Ανάλυση Κρατικού Προϋπολογισμού " + year);

        addOverview(year, revenues, expenses);
        addIndicators(balance, balancePct, primaryBalance, primaryPct, interestPct, investPct);
        addRisks(primaryPct, interestPct, investPct);
        addPolicy(balancePct, primaryPct, interestPct, investPct);
        addConclusion(year, balancePct, primaryPct, interestPct, investPct);
    }

    // =====================================================
    // OVERVIEW
    // =====================================================

    private void addOverview(int year, long rev, long exp) {

        addSection("Γενική Επισκόπηση Προϋπολογισμού");

        String text =
                "Η παρούσα ανάλυση εξετάζει τη συνολική δημοσιονομική εικόνα "
              + "του κρατικού προϋπολογισμού για το έτος " + year + ".\n\n"
              + "Τα συνολικά έσοδα ανέρχονται σε " + format(rev)
              + ", ενώ οι συνολικές δαπάνες διαμορφώνονται σε " + format(exp) + ".\n\n"
              + "Η σύγκριση αυτών των μεγεθών επιτρέπει την αξιολόγηση "
              + "της δημοσιονομικής ισορροπίας, της βιωσιμότητας "
              + "και της δυνατότητας άσκησης οικονομικής πολιτικής.";

        addCard(text);
    }

    // =====================================================
    // INDICATORS (ΒΕΛΤΙΩΜΕΝΟΙ)
    // =====================================================

    private void addIndicators(long bal, double balPct,
                               long prim, double primPct,
                               double intPct, double invPct) {

        addSection("Βασικοί Δημοσιονομικοί Δείκτες");

        String text =
                "• Συνολικό Δημοσιονομικό Ισοζύγιο\n"
              + "Δείχνει τη διαφορά μεταξύ συνολικών εσόδων και δαπανών.\n"
              + "Τιμή: " + format(bal) + " (" + pct(balPct) + " των εσόδων)\n"
              + explainBalance(balPct) + "\n\n"

              + "• Πρωτογενές Ισοζύγιο\n"
              + "Αποτυπώνει αν το κράτος καλύπτει τις βασικές λειτουργικές "
              + "του δαπάνες χωρίς να υπολογίζονται οι τόκοι χρέους.\n"
              + "Τιμή: " + format(prim) + " (" + pct(primPct) + " των εσόδων)\n"
              + explainPrimary(primPct) + "\n\n"

              + "• Δαπάνες Τόκων ως ποσοστό Εσόδων\n"
              + "Μετρά το βάρος εξυπηρέτησης του δημόσιου χρέους.\n"
              + "Τιμή: " + pct(intPct) + "\n"
              + explainInterest(intPct) + "\n\n"

              + "• Επενδυτικές Δαπάνες ως ποσοστό Εσόδων\n"
              + "Αξιολογεί τον βαθμό στήριξης της μελλοντικής ανάπτυξης.\n"
              + "Τιμή: " + pct(invPct) + "\n"
              + explainInvestment(invPct);

        addCard(text);
    }

    // =====================================================
    // RISKS
    // =====================================================

    private void addRisks(double primPct, double intPct, double invPct) {

        addSection("Ανάλυση Δημοσιονομικών Κινδύνων");

        String text =
                (primPct < 0
                        ? "• Αυξημένος δημοσιονομικός κίνδυνος λόγω αρνητικού πρωτογενούς αποτελέσματος.\n"
                        : "• Το πρωτογενές αποτέλεσμα περιορίζει τον άμεσο δημοσιονομικό κίνδυνο.\n") +
                (intPct > 12
                        ? "• Σημαντικός κίνδυνος από το υψηλό κόστος εξυπηρέτησης χρέους.\n"
                        : "• Το κόστος τόκων παραμένει σε διαχειρίσιμα επίπεδα.\n") +
                (invPct < 5
                        ? "• Μακροπρόθεσμος αναπτυξιακός κίνδυνος λόγω χαμηλών επενδύσεων."
                        : "• Οι επενδύσεις μειώνουν τους μελλοντικούς αναπτυξιακούς κινδύνους.");

        addCard(text);
    }

    // =====================================================
    // POLICY
    // =====================================================

    private void addPolicy(double balPct, double primPct,
                           double intPct, double invPct) {

        addSection("Κατευθύνσεις Δημοσιονομικής Πολιτικής");

        String text =
                (balPct < 0
                        ? "Η ελλειμματική θέση απαιτεί συγκράτηση δαπανών "
                          + "και ενίσχυση της αποτελεσματικότητας των εσόδων.\n\n"
                        : "Η δημοσιονομική ισορροπία επιτρέπει στοχευμένες "
                          + "αναπτυξιακές και κοινωνικές παρεμβάσεις.\n\n") +
                (invPct < 5
                        ? "Προτείνεται αναπροσανατολισμός πόρων προς παραγωγικές επενδύσεις."
                        : "Η επενδυτική πολιτική μπορεί να συνεχιστεί με σταθερό ρυθμό.");

        addCard(text);
    }

    // =====================================================
    // CONCLUSION
    // =====================================================

    private void addConclusion(int year, double balPct,
                               double primPct, double intPct, double invPct) {

        addSection("Τελικό Συμπέρασμα");

        String text =
                "Ο κρατικός προϋπολογισμός του " + year + " παρουσιάζει "
              + (balPct < 0 ? "δημοσιονομικές πιέσεις" : "σχετική σταθερότητα")
              + " και χαρακτηρίζεται από "
              + (primPct < 0 ? "διαρθρωτικές αδυναμίες." : "ικανοποιητική δημοσιονομική βάση.")
              + "\n\nΗ συνολική εικόνα καταδεικνύει την ανάγκη "
              + "ισορροπίας μεταξύ δημοσιονομικής πειθαρχίας, "
              + "αναπτυξιακής προοπτικής και κοινωνικής συνοχής.";

        addCard(text);
    }

    // =====================================================
    // UI HELPERS
    // =====================================================

    private void addTitle(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");
        contentBox.getChildren().add(l);
    }

    private void addSection(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-padding: 20 0 10 0;");
        contentBox.getChildren().add(l);
    }

    private void addCard(String text) {
        Label l = new Label(text);
        l.setWrapText(true);
        l.setStyle("-fx-font-size: 18px; -fx-line-spacing: 6px;");

        VBox box = new VBox(l);
        box.setStyle("""
                -fx-background-color: white;
                -fx-padding: 20;
                -fx-background-radius: 10;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 4);
                """);

        contentBox.getChildren().addAll(box, new Separator());
    }

    // =====================================================
    // UTILS
    // =====================================================

    private double percent(long v, long base) {
        return base == 0 ? 0 : (double) v / base * 100;
    }

    private String format(long v) {
        return String.format("%,d €", v);
    }

    private String pct(double v) {
        return String.format("%.2f%%", v);
    }

    private String explainBalance(double v) {
        if (v < -2) return "Δείχνει σοβαρό έλλειμμα και αυξημένη ανάγκη δανεισμού.";
        if (v < 0) return "Υποδηλώνει ήπια ελλειμματική δημοσιονομική θέση.";
        if (v < 2) return "Αντικατοπτρίζει σχετική δημοσιονομική ισορροπία.";
        return "Δημιουργεί δημοσιονομικό χώρο για παρεμβάσεις.";
    }

    private String explainPrimary(double v) {
        if (v < 0) return "Το κράτος δεν καλύπτει τις βασικές του δαπάνες χωρίς δανεισμό.";
        if (v < 2) return "Η πρωτογενής θέση είναι οριακά σταθερή.";
        return "Η πρωτογενής επίδοση ενισχύει τη βιωσιμότητα.";
    }

    private String explainInterest(double v) {
        if (v > 15) return "Το βάρος τόκων περιορίζει έντονα τη δημοσιονομική πολιτική.";
        if (v > 10) return "Το κόστος τόκων είναι σημαντικό αλλά διαχειρίσιμο.";
        return "Οι τόκοι δεν ασκούν σοβαρή πίεση.";
    }

    private String explainInvestment(double v) {
        if (v < 4) return "Χαμηλές επενδύσεις περιορίζουν τις μελλοντικές προοπτικές.";
        if (v < 7) return "Το επίπεδο επενδύσεων είναι μέτριο.";
        return "Οι επενδύσεις στηρίζουν τη μελλοντική ανάπτυξη.";
    }

    // =====================================================
    // BACK
    // =====================================================

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/MainView.fxml")
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.getScene().setRoot(root);   // ⬅ ΚΡΑΤΑΕΙ FULLSCREEN
            stage.setTitle("Dashboard");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}