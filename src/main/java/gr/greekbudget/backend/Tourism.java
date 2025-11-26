package gr.greekbudget.backend;

public class Tourism extends Expense {

    public Tourism(int category, double amount) {
        super(category, amount);
    }

    public static void main(String[] args) {
        Tourism tourismExpense = new Tourism(5, 39293000);
        System.out.println("Category: " + tourismExpense.getCategory());
        System.out.println("Amount: " + tourismExpense.getAmount());
    }
    
}
