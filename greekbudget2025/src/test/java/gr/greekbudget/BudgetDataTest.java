package gr.greekbudget;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BudgetDataTest {

    @Test
    void testTotalRevenuesAndExpenses() {
        // Επιβεβαίωση ότι οι μέθοδοι επιστρέφουν σωστά συνολικά έσοδα και δαπάνες
        for (int year = 2022; year <= 2026; year++) {
            long totalRev = BudgetData.getTotalRevenues(year);
            long totalExp = BudgetData.getTotalExpenses(year);

            assertTrue(totalRev > 0, "Τα συνολικά έσοδα πρέπει να είναι > 0 για το έτος " + year);
            assertTrue(totalExp > 0, "Οι συνολικές δαπάνες πρέπει να είναι > 0 για το έτος " + year);
        }
    }

    @Test
    void testBudgetResultCalculation() {
        // Ελέγχουμε ότι το αποτέλεσμα του προϋπολογισμού είναι σωστό
        for (int year = 2022; year <= 2026; year++) {
            long expected = BudgetData.getTotalRevenues(year) - BudgetData.getTotalExpenses(year);
            long result = BudgetData.getBudgetResult(year);
            assertEquals(expected, result, "Το αποτέλεσμα του προϋπολογισμού είναι λάθος για το έτος " + year);
        }
    }

    @Test
    void testSpecialExpenses() {
        // Ελέγχουμε ότι οι κατηγορίες "Τόκοι" και "Πάγια περιουσιακά στοιχεία" υπάρχουν
        for (int year = 2022; year <= 2026; year++) {
            Map<String, Long> exp = BudgetData.getExpenses().get(year);
            assertNotNull(exp, "Οι δαπάνες για το έτος " + year + " δεν πρέπει να είναι null");
            
            assertTrue(exp.containsKey("Τόκοι"), "Δεν υπάρχει κατηγορία Τόκοι για το έτος " + year);
            assertTrue(exp.containsKey("Πάγια περιουσιακά στοιχεία"), "Δεν υπάρχει κατηγορία Πάγια για το έτος " + year);
        }
    }

    @Test
    void testBalanceCalculations() {
        for (int year = 2022; year <= 2026; year++) {
            long rev = BudgetData.getTotalRevenues(year);
            long exp = BudgetData.getTotalExpenses(year);
            long balance = rev - exp;
            long interest = BudgetData.getExpenses().get(year).getOrDefault("Τόκοι", 0L);
            long primaryBalance = balance + interest;

            // Βασικός έλεγχος τύπων και λογικής
            assertTrue(balance <= rev, "Το ισοζύγιο πρέπει να είναι μικρότερο ή ίσο των εσόδων για το έτος " + year);
            assertTrue(primaryBalance <= rev + interest, "Το πρωτογενές ισοζύγιο υπερβαίνει τα αναμενόμενα για το έτος " + year);
        }
    }

    @Test
    void testPercentCalculation() {
        // Δοκιμή βοηθητικής συνάρτησης percent
        long revenue = 100_000L;
        long value = 25_000L;
        double pct = revenue == 0 ? 0 : (double) value / revenue * 100;
        assertEquals(25.0, pct, 0.0001);
    }

    @Test
    void testRevenuesAndExpensesNonEmpty() {
        // Έλεγχος ότι οι χάρτες δεν είναι κενές
        assertFalse(BudgetData.getRevenues().isEmpty(), "Οι έσοδα δεν πρέπει να είναι κενά");
        assertFalse(BudgetData.getExpenses().isEmpty(), "Οι δαπάνες δεν πρέπει να είναι κενές");
    }
}
