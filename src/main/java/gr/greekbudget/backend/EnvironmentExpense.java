package gr.greekbudget.backend;

public class EnvironmentExpense extends Expense {

    public EnvironmentExpense(int category, double amount) {
        super(category, amount);
    }

    public static void main(String[] args) {
        EnvironmentExpense envExpense = new EnvironmentExpense(6, 4545000000);
        System.out.println("Category: " + envExpense.getCategory());
        System.out.println("Amount: " + envExpense.getAmount());
    }   
}
