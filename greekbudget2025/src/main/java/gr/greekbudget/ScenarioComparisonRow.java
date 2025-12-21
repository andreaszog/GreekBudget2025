package gr.greekbudget;

import javafx.beans.property.*;

public class ScenarioComparisonRow {

    private final IntegerProperty year = new SimpleIntegerProperty();
    private final StringProperty ministry = new SimpleStringProperty();
    private final LongProperty oldAmount = new SimpleLongProperty();
    private final LongProperty newAmount = new SimpleLongProperty();

    public ScenarioComparisonRow(
            int year,
            String ministry,
            long oldAmount,
            long newAmount
    ) {
        this.year.set(year);
        this.ministry.set(ministry);
        this.oldAmount.set(oldAmount);
        this.newAmount.set(newAmount);
    }

    // ===== GETTERS =====
    public int getYear() {
        return year.get();
    }

    public String getMinistry() {
        return ministry.get();
    }

    public long getOldAmount() {
        return oldAmount.get();
    }

    public long getNewAmount() {
        return newAmount.get();
    }

    // ===== PROPERTIES (για TableView) =====
    public IntegerProperty yearProperty() {
        return year;
    }

    public StringProperty ministryProperty() {
        return ministry;
    }

    public LongProperty oldAmountProperty() {
        return oldAmount;
    }

    public LongProperty newAmountProperty() {
        return newAmount;
    }

    // ===== ΥΠΟΛΟΓΙΣΜΟΙ =====
    public long getDelta() {
        return newAmount.get() - oldAmount.get();
    }

    public LongProperty deltaProperty() {
        return new SimpleLongProperty(getDelta());
    }

    public double getDeltaPct() {
        if (oldAmount.get() == 0) return 0;
        return getDelta() * 100.0 / oldAmount.get();
    }

    public DoubleProperty deltaPctProperty() {
        return new SimpleDoubleProperty(getDeltaPct());
    }
}
