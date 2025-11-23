package gr.greekbudget.backend;

public class Revenue {

    private String category;
    private double amount;

    public Revenue(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public double getAmount() {
        return amount;
    }
}
