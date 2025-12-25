package gr.greekbudget;

import javafx.beans.property.*;

public class RevExpScenarioRow {

    private final IntegerProperty year = new SimpleIntegerProperty();
    private final StringProperty type = new SimpleStringProperty();   // ΕΣΟΔΑ / ΕΞΟΔΑ
    private final StringProperty name = new SimpleStringProperty();   // Κατηγορία
    private final LongProperty oldAmount = new SimpleLongProperty();
    private final ObjectProperty<Long> newAmount =
            new SimpleObjectProperty<>(null);

    public RevExpScenarioRow(
            int year,
            String type,
            String name,
            long oldAmount
    ) {
        this.year.set(year);
        this.type.set(type);
        this.name.set(name);
        this.oldAmount.set(oldAmount);
    }

    // ===== GETTERS =====
    public int getYear() { return year.get(); }
    public String getType() { return type.get(); }
    public String getName() { return name.get(); }
    public long getOldAmount() { return oldAmount.get(); }
    public Long getNewAmount() { return newAmount.get(); }

    // ===== PROPERTIES =====
    public IntegerProperty yearProperty() { return year; }
    public StringProperty typeProperty() { return type; }
    public StringProperty nameProperty() { return name; }
    public LongProperty oldAmountProperty() { return oldAmount; }
    public ObjectProperty<Long> newAmountProperty() { return newAmount; }

    public void setNewAmount(Long v) {
        newAmount.set(v);
    }

    // ===== KEY (όπως στα Υπουργεία) =====
    public String key() {
        return year.get() + "||" + type.get() + "||" + name.get();
    }
}
