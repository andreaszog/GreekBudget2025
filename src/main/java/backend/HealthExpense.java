package backend;

public class HealthExpense extends Expense {

    public HealthExpense(double amount) {
        super("Health", amount);
    }

    public static void main(String[] args) {
        HealthExpense healthExpense = new HealthExpense(13450000000);
        System.out.println("Category: " + healthExpense.getCategory());
        System.out.println("Amount: " + healthExpense.getAmount() + " €");
    }
} {
    
}
