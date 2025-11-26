package gr.greekbudget.backend;

public  class DefenseExpense extends Expense  {
    
    public DefenseExpense(int category, double amount) {
        super(category, amount);
    }

    public static void main(String[] args) {
        DefenseExpense defenseExpense = new DefenseExpense(1, 115306000000);
        System.out.println("Category: " + defenseExpense.getCategory());
        System.out.println("Amount: " + defenseExpense.getAmount());
    }
}
