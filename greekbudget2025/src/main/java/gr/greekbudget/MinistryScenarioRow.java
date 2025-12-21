package gr.greekbudget;

import javafx.beans.property.*;

public class MinistryScenarioRow {

    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final StringProperty ministry = new SimpleStringProperty();
    private final LongProperty originalAmount = new SimpleLongProperty();
    private final LongProperty newAmount = new SimpleLongProperty();

    public MinistryScenarioRow(String ministry, long amount) {
        this.ministry.set(ministry);
        this.originalAmount.set(amount);
        this.newAmount.set(amount);
    }

    public BooleanProperty selectedProperty() { return selected; }
    public StringProperty ministryProperty() { return ministry; }
    public LongProperty originalAmountProperty() { return originalAmount; }
    public LongProperty newAmountProperty() { return newAmount; }

    public boolean isSelected() { return selected.get(); }
    public void setSelected(boolean v) { selected.set(v); }

    public long getOriginalAmount() { return originalAmount.get(); }
    public long getNewAmount() { return newAmount.get(); }
    public void setNewAmount(long v) { newAmount.set(v); }

    public long getDelta() {
        return newAmount.get() - originalAmount.get();
    }

    public double getDeltaPct() {
        if (originalAmount.get() == 0) return 0;
        return getDelta() * 100.0 / originalAmount.get();
    }
}
