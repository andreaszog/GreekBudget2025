package gr.greekbudget;

public class BudgetModel {

    private String category;
    private double amount;
    private boolean isInocme;

    public BudgetDataModel(String category, double amount, boolean isIncome) {
        this.category = category;
        this.amount = amount;
        this.isIncome = isIncome;

    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isIncome() {
        return isIncome;
    }

    @Override
    public String toString() {
        return (isIncome ? "Έσοδο" : "Έξοδο") + " | " + category + " | " + amount + "€";
    }
}
