package gr.greekbudget;

import javafx.beans.property.*;

public class MinistryRow {

    private final IntegerProperty year = new SimpleIntegerProperty();
    private final StringProperty ministry = new SimpleStringProperty();
    private final LongProperty amount = new SimpleLongProperty();
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public MinistryRow(int year, String ministry, long amount) {
        this.year.set(year);
        this.ministry.set(ministry);
        this.amount.set(amount);
    }

    public int getYear() { return year.get(); }
    public IntegerProperty yearProperty() { return year; }

    public String getMinistry() { return ministry.get(); }
    public StringProperty ministryProperty() { return ministry; }

    public long getAmount() { return amount.get(); }
    public LongProperty amountProperty() { return amount; }

    public boolean isSelected() { return selected.get(); }
    public BooleanProperty selectedProperty() { return selected; }
    public void setSelected(boolean value) { selected.set(value); }

    public String key() {
        return year.get() + "||" + ministry.get();
    }
}
