package gr.greekbudget;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryAmountTest {

    @Test
    void constructorInitializesPropertiesCorrectly() {
        CategoryAmount ca = new CategoryAmount("Υγεία", 1500L);

        assertEquals("Υγεία", ca.getCategory());
        assertEquals(1500L, ca.getAmount());
    }

    @Test
    void settersUpdateValuesCorrectly() {
        CategoryAmount ca = new CategoryAmount("Φαγητό", 500L);

        ca.setCategory("Εκπαίδευση");
        ca.setAmount(2000L);

        assertEquals("Εκπαίδευση", ca.getCategory());
        assertEquals(2000L, ca.getAmount());
    }
}
