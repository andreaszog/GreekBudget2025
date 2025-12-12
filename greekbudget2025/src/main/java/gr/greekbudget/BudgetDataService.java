package gr.greekbudget;

import java.util.LinkedHashMap;
import java.util.Map;

public class BudgetDataService {

    // ====== ΕΣΟΔΑ ======
    private static final Map<String, Long> incomeData = new LinkedHashMap<>();

    // ====== ΕΞΟΔΑ ======
    private static final Map<String, Long> expenseData = new LinkedHashMap<>();

    static {
        // ===== ΔΕΔΟΜΕΝΑ ΑΠΟ ΤΟ WORD (ΠΡΟΣΩΡΙΝΑ ΣΤΑΤΙΚΑ) =====

        // ΕΣΟΔΑ
        incomeData.put("Φορολογικά Έσοδα", 50000000000L);
        incomeData.put("Έμμεσοι Φόροι", 30000000000L);
        incomeData.put("Ασφαλιστικές Εισφορές", 25000000000L);
        incomeData.put("Λοιπά Έσοδα", 10000000000L);

        // ΕΞΟΔΑ
        expenseData.put("Μισθοί & Συντάξεις", 60000000000L);
        expenseData.put("Υγεία & Παιδεία", 35000000000L);
        expenseData.put("Άμυνα", 15000000000L);
        expenseData.put("Κοινωνικές Παροχές", 20000000000L);
    }

    // ================= GETTERS =================

    public static Map<String, Long> getIncomeData() {
        return incomeData;
    }

    public static Map<String, Long> getExpenseData() {
        return expenseData;
    }
}
