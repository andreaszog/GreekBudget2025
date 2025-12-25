package gr.greekbudget;

import javafx.beans.property.*;

public class RevExpScenarioComparisonRow {

    private final IntegerProperty year = new SimpleIntegerProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final LongProperty oldAmount = new SimpleLongProperty();
    private final LongProperty newAmount = new SimpleLongProperty();

    private final ReadOnlyLongWrapper delta = new ReadOnlyLongWrapper();
    private final ReadOnlyDoubleWrapper deltaPct = new ReadOnlyDoubleWrapper();

    public RevExpScenarioComparisonRow(
            int year,
            String type,
            String name,
            long oldAmount,
            long newAmount
    ) {
        this.year.set(year);
        this.type.set(type);
        this.name.set(name);
        this.oldAmount.set(oldAmount);
        this.newAmount.set(newAmount);

        recompute();
    }

    private void recompute() {
        long d = newAmount.get() - oldAmount.get();
        delta.set(d);
        deltaPct.set(oldAmount.get() == 0 ? 0 : d * 100.0 / oldAmount.get());
    }

    public int getYear() { return year.get(); }
    public String getType() { return type.get(); }
    public String getName() { return name.get(); }
    public long getOldAmount() { return oldAmount.get(); }
    public long getNewAmount() { return newAmount.get(); }
    public long getDelta() { return delta.get(); }
    public double getDeltaPct() { return deltaPct.get(); }

    public IntegerProperty yearProperty() { return year; }
    public StringProperty typeProperty() { return type; }
    public StringProperty nameProperty() { return name; }
    public LongProperty oldAmountProperty() { return oldAmount; }
    public LongProperty newAmountProperty() { return newAmount; }
    public ReadOnlyLongProperty deltaProperty() { return delta.getReadOnlyProperty(); }
    public ReadOnlyDoubleProperty deltaPctProperty() { return deltaPct.getReadOnlyProperty(); }
}
