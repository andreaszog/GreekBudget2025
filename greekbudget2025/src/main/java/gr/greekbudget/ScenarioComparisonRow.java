package gr.greekbudget;

import javafx.beans.property.*;

public class ScenarioComparisonRow {

    private final IntegerProperty year = new SimpleIntegerProperty();
    private final StringProperty ministry = new SimpleStringProperty();
    private final LongProperty oldAmount = new SimpleLongProperty();
    private final LongProperty newAmount = new SimpleLongProperty();

    // computed read-only
    private final ReadOnlyLongWrapper delta = new ReadOnlyLongWrapper();
    private final ReadOnlyDoubleWrapper deltaPct = new ReadOnlyDoubleWrapper();

    public ScenarioComparisonRow(int year, String ministry, long oldAmount, long newAmount) {
        this.year.set(year);
        this.ministry.set(ministry);
        this.oldAmount.set(oldAmount);
        this.newAmount.set(newAmount);

        recompute();

        this.oldAmount.addListener((obs, o, n) -> recompute());
        this.newAmount.addListener((obs, o, n) -> recompute());
    }

    private void recompute() {
        long d = getNewAmount() - getOldAmount();
        delta.set(d);

        double pct = (getOldAmount() == 0) ? 0.0 : (d * 100.0 / getOldAmount());
        deltaPct.set(pct);
    }

    public int getYear() { return year.get(); }
    public String getMinistry() { return ministry.get(); }
    public long getOldAmount() { return oldAmount.get(); }
    public long getNewAmount() { return newAmount.get(); }

    public IntegerProperty yearProperty() { return year; }
    public StringProperty ministryProperty() { return ministry; }
    public LongProperty oldAmountProperty() { return oldAmount; }
    public LongProperty newAmountProperty() { return newAmount; }

    public long getDelta() { return delta.get(); }
    public ReadOnlyLongProperty deltaProperty() { return delta.getReadOnlyProperty(); }

    public double getDeltaPct() { return deltaPct.get(); }
    public ReadOnlyDoubleProperty deltaPctProperty() { return deltaPct.getReadOnlyProperty(); }
}
