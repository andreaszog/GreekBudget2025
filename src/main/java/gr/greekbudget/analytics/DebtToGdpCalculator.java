package gr.greekbudget.analytics;

public class DebtToGdpCalculator {

    public double calculate(double debt, double gdp) {
        if (gdp == 0) throw new IllegalArgumentException("GDP cannot be zero");
        return (debt / gdp) * 100;
    }
}
