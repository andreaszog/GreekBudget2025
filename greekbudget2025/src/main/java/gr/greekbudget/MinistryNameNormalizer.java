package gr.greekbudget;

import java.util.Map;

public class MinistryNameNormalizer {

    // canonicalName -> displayName
    private static final Map<String, String> CANONICAL = Map.ofEntries(

        Map.entry(
            "Υπουργείο Εθνικής Οικονομίας και Οικονομικών",
            "Υπουργείο Οικονομικών (πρώην)"
        ),
        Map.entry(
            "Υπουργείο Εσωτερικών",
            "Υπουργείο Εσωτερικών"
        ),
        Map.entry(
            "Υπουργείο Εθνικής Άμυνας",
            "Υπουργείο Εθνικής Άμυνας"
        ),
        Map.entry(
            "Υπουργείο Υγείας",
            "Υπουργείο Υγείας"
        ),
        Map.entry(
            "Υπουργείο Παιδείας",
            "Υπουργείο Παιδείας"
        ),
        Map.entry(
            "Υπουργείο Εργασίας και Κοινωνικών Υποθέσεων",
            "Υπουργείο Εργασίας"
        ),
        Map.entry(
            "Υπουργείο Υποδομών και Μεταφορών",
            "Υπουργείο Υποδομών"
        ),
        Map.entry(
            "Υπουργείο Περιβάλλοντος και Ενέργειας",
            "Υπουργείο Περιβάλλοντος"
        ),
        Map.entry(
            "Υπουργείο Πολιτισμού",
            "Υπουργείο Πολιτισμού"
        ),
        Map.entry(
            "Υπουργείο Μετανάστευσης και Ασύλου",
            "Υπουργείο Μετανάστευσης"
        )
    );

    // Μετατρέπει παλιά / νέα ονόματα στο canonical
    public static String canonical(String raw) {

        if (raw.contains("Οικονομ")) {
            return "Υπουργείο Εθνικής Οικονομίας και Οικονομικών";
        }

        return CANONICAL.keySet().stream()
                .filter(raw::contains)
                .findFirst()
                .orElse(raw);
    }

    // Τι δείχνουμε στο UI
    public static String displayLabel(String canonical) {
        return CANONICAL.getOrDefault(canonical, canonical);
    }
}
