package gr.greekbudget;

import java.util.LinkedHashMap;
import java.util.Map;

public class BudgetData {

    // year -> (category -> amount)
    private static final Map<Integer, Map<String, Long>> revenues = new LinkedHashMap<>();
    private static final Map<Integer, Map<String, Long>> expenses = new LinkedHashMap<>();

    static {
        // =========================
        // ====== ΕΤΟΣ 2026 ========
        // =========================

        Map<String, Long> rev2026 = new LinkedHashMap<>();
        rev2026.put("Φόροι", 65_586_000_000L);
        rev2026.put("Κοινωνικές εισφορές", 60_000_000L);
        rev2026.put("Μεταβιβάσεις", 10_943_000_000L);
        rev2026.put("Πωλήσεις αγαθών και υπηρεσιών", 1_201_000_000L);
        rev2026.put("Λοιπά τρέχοντα έσοδα", 2_407_000_000L);
        rev2026.put("Πάγια περιουσιακά στοιχεία", 51_000_000L);
        rev2026.put("Χρεωστικοί τίτλοι", 11_000_000L);
        rev2026.put("Δάνεια", 5_525_000_000L);
        rev2026.put("Συμμετοχικοί τίτλοι & μερίδια", 228_000_000L);
        rev2026.put("Υποχρεώσεις από νόμισμα & καταθέσεις", 63_000_000L);
        rev2026.put("Χρεωστικοί τίτλοι (υποχρεώσεις)", 23_875_000_000L);
        rev2026.put("Δάνεια (χρηματοδότηση)", 1_654_726_000_000L);
        rev2026.put("Χρηματοοικονομικά παράγωγα", 959_000_000L);

        revenues.put(2026, rev2026);

        Map<String, Long> exp2026 = new LinkedHashMap<>();
        exp2026.put("Παροχές σε εργαζομένους", 15_688_490_000L);
        exp2026.put("Κοινωνικές παροχές", 627_779_000L);
        exp2026.put("Μεταβιβάσεις", 35_897_013_000L);
        exp2026.put("Αγορές αγαθών & υπηρεσιών", 2_388_349_000L);
        exp2026.put("Επιδοτήσεις", 80_330_000L);
        exp2026.put("Τόκοι", 7_917_420_000L);
        exp2026.put("Λοιπές δαπάνες", 101_259_000L);
        exp2026.put("Πιστώσεις υπό κατανομή", 19_989_259_000L);
        exp2026.put("Πάγια περιουσιακά στοιχεία", 3_355_541_000L);
        exp2026.put("Τιμαλφή", 84_000L);
        exp2026.put("Δάνεια", 12_079_352_000L);
        exp2026.put("Συμμετοχικοί τίτλοι & μερίδια", 1_587_084_000L);
        exp2026.put("Χρεωστικοί τίτλοι (υποχρεώσεις)", 24_909_729_000L);
        exp2026.put("Δάνεια (αποπληρωμή)", 1_659_113_701_000L);
        exp2026.put("Χρηματοοικονομικά παράγωγα", 341_662_000L);

        expenses.put(2026, exp2026);
    }

    public static Map<Integer, Map<String, Long>> getRevenues() {
        return revenues;
    }

    public static Map<Integer, Map<String, Long>> getExpenses() {
        return expenses;
    }

    public static long getTotalRevenues(int year) {
        return revenues.getOrDefault(year, Map.of())
                .values().stream().mapToLong(Long::longValue).sum();
    }

    public static long getTotalExpenses(int year) {
        return expenses.getOrDefault(year, Map.of())
                .values().stream().mapToLong(Long::longValue).sum();
    }
}
