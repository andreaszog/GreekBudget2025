package gr.greekbudget;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class MoneyUtil {

    private static final DecimalFormatSymbols EURO_SYMBOLS;
    private static final DecimalFormat EURO_FORMAT;
    private static final DecimalFormat PCT_FORMAT;

    static {
        EURO_SYMBOLS = new DecimalFormatSymbols(Locale.GERMANY);
        EURO_SYMBOLS.setGroupingSeparator('.');
        EURO_SYMBOLS.setDecimalSeparator(',');

        EURO_FORMAT = new DecimalFormat("#,###", EURO_SYMBOLS);
        EURO_FORMAT.setGroupingUsed(true);

        PCT_FORMAT = new DecimalFormat("0.0", DecimalFormatSymbols.getInstance(Locale.US));
    }

    private MoneyUtil() {}

    public static String formatEuro(long value) {
        return EURO_FORMAT.format(value) + " €";
    }

    public static String formatSignedEuro(long value) {
        if (value > 0) {
            return "+" + EURO_FORMAT.format(value) + " €";
        } else if (value < 0) {
            return "−" + EURO_FORMAT.format(Math.abs(value)) + " €";
        } else {
            return "0 €";
        }
    }

    public static String formatPct(double value) {
        return PCT_FORMAT.format(value) + "%";
    }

    public static long parseEuroToLong(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Invalid: null");
        }

        String cleaned = input.trim()
                .replace("€", "")
                .replace(" ", "")
                .replace(".", "")
                .replace(",", ".");

        if (cleaned.isEmpty() || !cleaned.matches("-?\\d+(\\.\\d+)?")) {
            throw new IllegalArgumentException("Invalid: " + input);
        }

        return Math.round(Double.parseDouble(cleaned));
    }
}
