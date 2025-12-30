package gr.greekbudget;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BudgetImpactRowTest {

    @Test
    void constructorAndGettersWorkCorrectly() {
        BudgetImpactRow row = new BudgetImpactRow(2025, 1000L, 1500L);

        assertEquals(2025, row.getYear());
        assertEquals(1000L, row.getBefore());
        assertEquals(1500L, row.getAfter());
    }

    @Test
    void getDeltaCalculatesCorrectly() {
        BudgetImpactRow row = new BudgetImpactRow(2025, 1000L, 1500L);
        assertEquals(500L, row.getDelta());

        BudgetImpactRow rowNegative = new BudgetImpactRow(2025, 1500L, 1000L);
        assertEquals(-500L, rowNegative.getDelta());
    }

    @Test
    void improvedReturnsTrueWhenAfterGreaterThanBefore() {
        BudgetImpactRow row = new BudgetImpactRow(2025, 1000L, 1500L);
        assertTrue(row.improved());
    }

    @Test
    void improvedReturnsFalseWhenAfterLessOrEqualBefore() {
        BudgetImpactRow rowEqual = new BudgetImpactRow(2025, 1000L, 1000L);
        assertFalse(rowEqual.improved());

        BudgetImpactRow rowLess = new BudgetImpactRow(2025, 1500L, 1000L);
        assertFalse(rowLess.improved());
    }
}
