package gr.greekbudget;

import javafx.beans.property.*;

public class ScenarioRow {

    private final IntegerProperty year = new SimpleIntegerProperty();
    private final StringProperty ministry = new SimpleStringProperty();
    private final LongProperty oldAmount = new SimpleLongProperty();
    private final ObjectProperty<Long> newAmount = new SimpleObjectProperty<>(null);

    public ScenarioRow(int year, String ministry, long oldAmount) {
        this.year.set(year);
        this.ministry.set(ministry);
        this.oldAmount.set(oldAmount);
    }

    public int getYear() { return year.get(); }
    public IntegerProperty yearProperty() { return year; }

    public String getMinistry() { return ministry.get(); }
    public StringProperty ministryProperty() { return ministry; }

    public long getOldAmount() { return oldAmount.get(); }
    public LongProperty oldAmountProperty() { return oldAmount; }

    public Long getNewAmount() { return newAmount.get(); }
    public ObjectProperty<Long> newAmountProperty() { return newAmount; }
    public void setNewAmount(Long v) { newAmount.set(v); }

    public String key() {
        return year.get() + "||" + ministry.get();
    }
}
