package backend;

public class DefenseExpense extends Expense {
    
    public DefenseExpense(double amount) {
        super("Defense", amount);
    }

    public static void main(String[] args) {
        DefenseExpense defenseExpense = new DefenseExpense(115306000000);
        System.out.println("Category: " + defenseExpense.getCategory());
        System.out.println("Amount: " + defenseExpense.getAmount() + " €");
    }
}
