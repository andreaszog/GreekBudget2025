package gr.greekbudget;

import javafx.beans.property.*;

public class CategoryAmount {
    private final StringProperty category;
    private final LongProperty amount;

    public CategoryAmount(String category, Long amount) {
        this.category = new SimpleStringProperty(category);
        this.amount = new SimpleLongProperty(amount);
    }

    public String getCategory() { return category.get(); }
    public void setCategory(String value) { category.set(value); }
    public StringProperty categoryProperty() { return category; }

    public long getAmount() { return amount.get(); }
    public void setAmount(long value) { amount.set(value); }
    public LongProperty amountProperty() { return amount; }
}
