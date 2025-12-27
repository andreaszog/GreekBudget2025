package gr.greekbudget;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoneyUtilTest {

    @Test
    void formatEuro_formatsCorrectly() {
        assertEquals("1.234.567 €", MoneyUtil.formatEuro(1_234_567));
        assertEquals("0 €", MoneyUtil.formatEuro(0));
    }

    @Test
    void formatSignedEuro_formatsPositiveNegativeAndZero() {
        assertEquals("+1.000 €", MoneyUtil.formatSignedEuro(1_000));
        assertEquals("−1.000 €", MoneyUtil.formatSignedEuro(-1_000));
        assertEquals("0 €", MoneyUtil.formatSignedEuro(0));
    }

    @Test
    void formatPct_formatsWithOneDecimal() {
        assertEquals("12.5%", MoneyUtil.formatPct(12.456));
        assertEquals("0.0%", MoneyUtil.formatPct(0));
    }

    @Test
    void parseEuro_parsesCorrectValues() {
        assertEquals(1_234_567L, MoneyUtil.parseEuroToLong("1.234.567 €"));
        assertEquals(1000L, MoneyUtil.parseEuroToLong("1.000"));
        assertEquals(0L, MoneyUtil.parseEuroToLong("0 €"));
    }

    @Test
    void parseEuro_throwsOnInvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> MoneyUtil.parseEuroToLong("abc"));

        assertThrows(IllegalArgumentException.class,
                () -> MoneyUtil.parseEuroToLong(""));
    }
}
