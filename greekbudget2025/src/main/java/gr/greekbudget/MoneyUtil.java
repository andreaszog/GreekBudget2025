package gr.greekbudget;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class MoneyUtil {
    private MoneyUtil() {}

    private static final DecimalFormat EUR_FMT;
    static {
        DecimalFormatSymbols sym = new DecimalFormatSymbols(new Locale("el", "GR"));
        sym.setGroupingSeparator('.');
        sym.setDecimalSeparator(',');
        EUR_FMT = new DecimalFormat("#,##0", sym);
    }

    public static String formatEuro(long v) {
        return EUR_FMT.format(v) + " €";
    }

    /**
     * Accepts inputs like:
     * "1.234.567", "1,234,567", "1234567", "1.234.567 €"
     */
    public static long parseEuroToLong(String s) {
        if (s == null) return 0L;
        String cleaned = s.trim()
                .replace("€", "")
                .replace(" ", "")
                .replace(".", "")
                .replace(",", "");
        if (cleaned.isEmpty() || cleaned.equals("-")) return 0L;
        return Long.parseLong(cleaned);
    }

    public static String formatSignedEuro(long delta) {
        String sign = delta > 0 ? "+" : "";
        return sign + formatEuro(delta);
    }

    public static String formatPct(double pct) {
        return String.format(Locale.US, "%.2f%%", pct);
    }
}
