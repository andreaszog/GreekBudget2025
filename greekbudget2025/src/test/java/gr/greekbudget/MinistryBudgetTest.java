package gr.greekbudget;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MinistryBudgetTest {

    @Test
    void testCanonicalMapping() {
        // Παλιά / νέα ονόματα πρέπει να γυρίζουν canonical
        assertEquals("Υπουργείο Εθνικής Οικονομίας και Οικονομικών",
                MinistryNameNormalizer.canonical("Υπουργείο Οικονομικών"));
        assertEquals("Υπουργείο Εσωτερικών",
                MinistryNameNormalizer.canonical("Υπουργείο Εσωτερικών"));
        assertEquals("Υπουργείο Υγείας",
                MinistryNameNormalizer.canonical("Υπουργείο Υγείας"));
    }

    @Test
    void testDisplayLabel() {
        // canonical -> display
        assertEquals("Υπουργείο Οικονομικών (πρώην)",
                MinistryNameNormalizer.displayLabel("Υπουργείο Εθνικής Οικονομίας και Οικονομικών"));
        assertEquals("Υπουργείο Εσωτερικών",
                MinistryNameNormalizer.displayLabel("Υπουργείο Εσωτερικών"));
        // αν δεν υπάρχει, επιστρέφει το ίδιο
        assertEquals("Άγνωστο Υπουργείο",
                MinistryNameNormalizer.displayLabel("Άγνωστο Υπουργείο"));
    }

    @Test
    void testBudgetDataForYear() {
        // Υπάρχουν δεδομένα για όλα τα έτη
        for (int year = 2022; year <= 2026; year++) {
            Map<String, Long> totals = MinistryBudgetData.getTotalsForYear(year);
            assertNotNull(totals, "Year " + year + " should have budget data");
            assertFalse(totals.isEmpty(), "Year " + year + " totals should not be empty");
        }
    }

    @Test
    void testSpecificBudgetValues() {
        // Δοκιμή συγκεκριμένων τιμών
        Long health2026 = MinistryBudgetData.getTotal(2026, "Υπουργείο Υγείας");
        assertNotNull(health2026);
        assertEquals(7_841_945_000L, health2026);

        Long finance2025 = MinistryBudgetData.getTotal(2025, "Υπουργείο Εθνικής Οικονομίας και Οικονομικών");
        assertEquals(1_246_518_464_000L, finance2025);
    }

    @Test
    void testAvailableYears() {
        var years = MinistryBudgetData.getAvailableYears();
        assertTrue(years.contains(2022));
        assertTrue(years.contains(2026));
        assertEquals(5, years.size());
    }
}
