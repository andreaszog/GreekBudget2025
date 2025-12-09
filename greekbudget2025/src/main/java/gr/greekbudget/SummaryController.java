package gr.greekbudget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;


import java.util.*;

public class SummaryController {

    @FXML private ComboBox<String> yearComboBox;
    @FXML private ComboBox<String> ministryComboBox;
    @FXML private VBox summaryBox;

    private final Map<String, Map<String, Object>> budgetData = new HashMap<>();

    @FXML
    public void initialize() {
        yearComboBox.getItems().addAll("2026");
        yearComboBox.getSelectionModel().selectFirst();

        loadMockBudget();

        updateMinistryOptions("2026");
    }

    private void updateMinistryOptions(String year) {
        ministryComboBox.getItems().clear();
        ministryComboBox.getItems().add("Όλα τα υπουργεία");

        Map<String, Object> yearData = budgetData.get(year);
        if (yearData != null && yearData.containsKey("Υπουργεία")) {
            Map<String, Object> ministries = (Map<String, Object>) yearData.get("Υπουργεία");
            ministryComboBox.getItems().addAll(ministries.keySet());
        }

        ministryComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void loadSummary() {
        summaryBox.getChildren().clear();

        String year = yearComboBox.getValue();
        String ministry = ministryComboBox.getValue();

        Map<String, Object> yearData = budgetData.get(year);
        if (yearData == null) return;

        if ("Όλα τα υπουργεία".equals(ministry)) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) yearData.get("Υπουργεία")).entrySet()) {
                summaryBox.getChildren().add(new Label(entry.getKey()));
            }
        } else {
            Object data = ((Map<String, Object>) yearData.get("Υπουργεία")).get(ministry);
            summaryBox.getChildren().add(new Label("Στοιχεία για: " + ministry));
            summaryBox.getChildren().add(new Label(data.toString()));
        }

    }

    private void loadMockBudget() {
        Map<String, Object> year2026 = new HashMap<>();
        Map<String, Object> ministries = new LinkedHashMap<>();

        Map<String, Object> health = new HashMap<>();
        health.put("Παροχές σε εργαζομένους", 2389600000L);
        health.put("Μεταβιβάσεις", 4448303000L);
        health.put("Αγορές αγαθών και υπηρεσιών", 104599000L);
        ministries.put("Υπουργείο Υγείας", health);

        Map<String, Object> defense = new HashMap<>();
        defense.put("Παροχές σε εργαζομένους", 2949251000L);
        defense.put("Μεταβιβάσεις", 54602000L);
        defense.put("Εξοπλισμοί", 3249876000L);
        ministries.put("Υπουργείο Εθνικής Άμυνας", defense);

        Map<String, Object> education = new HashMap<>();
        education.put("Παροχές σε εργαζομένους", 5214377000L);
        education.put("Μεταβιβάσεις", 556318000L);
        education.put("Αγορές αγαθών και υπηρεσιών", 125811000L);
        ministries.put("Υπουργείο Παιδείας", education);

        Map<String, Object> justice = new HashMap<>();
        justice.put("Παροχές σε εργαζομένους", 571818000L);
        justice.put("Μεταβιβάσεις", 8995000L);
        justice.put("Αγορές αγαθών και υπηρεσιών", 34975000L);
        ministries.put("Υπουργείο Δικαιοσύνης", justice);

        Map<String, Object> interior = new HashMap<>();
        interior.put("Παροχές σε εργαζομένους", 68331000L);
        interior.put("Μεταβιβάσεις", 3605788000L);
        interior.put("Αγορές αγαθών και υπηρεσιών", 53165000L);
        ministries.put("Υπουργείο Εσωτερικών", interior);

        year2026.put("Υπουργεία", ministries);
        budgetData.put("2026", year2026);
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

            stage.setScene(scene);
            stage.show();

         } catch (Exception e) {
             e.printStackTrace();
        }
    }
}