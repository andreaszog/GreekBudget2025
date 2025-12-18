package gr.greekbudget.backend;

public class Revenue {
    private String category;
    private double amount;

    public Revenue(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}
