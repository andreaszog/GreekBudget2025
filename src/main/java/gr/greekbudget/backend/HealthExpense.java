package gr.greekbudget.backend;

public class HealthExpense extends Expense {

    public HealthExpense(int category, double amount) {
        super(category, amount);
    }

    public static void main(String[] args) {
        HealthExpense healthExpense = new HealthExpense(1, 13450000000);
        System.out.println("Category: " + healthExpense.getCategory());
        System.out.println("Amount: " + healthExpense.getAmount());
    }

}
