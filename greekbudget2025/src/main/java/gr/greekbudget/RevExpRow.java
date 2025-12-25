package gr.greekbudget;

import javafx.beans.property.*;

public class RevExpRow {

    private final IntegerProperty year = new SimpleIntegerProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final LongProperty amount = new SimpleLongProperty();
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public RevExpRow(int year, String type, String name, long amount) {
        this.year.set(year);
        this.type.set(type);
        this.name.set(name);
        this.amount.set(amount);
    }

    public int getYear() { return year.get(); }
    public String getType() { return type.get(); }
    public String getName() { return name.get(); }
    public long getAmount() { return amount.get(); }

    public IntegerProperty yearProperty() { return year; }
    public StringProperty typeProperty() { return type; }
    public StringProperty nameProperty() { return name; }
    public LongProperty amountProperty() { return amount; }

    public BooleanProperty selectedProperty() { return selected; }
    public void setSelected(boolean v) { selected.set(v); }

    public String key() {
        return year.get() + "|" + type.get() + "|" + name.get();
    }
}
