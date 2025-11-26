package gr.greekbudget.backend;

public class Expense {
      private int category;
    private double amount;

    public Expense(int category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public int getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}
