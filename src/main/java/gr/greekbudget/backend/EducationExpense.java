package gr.greekbudget.backend;

public  EducationExpense extends Expense {
    
    public EducationExpense(int category, double amount) {
        super(category, amount);
    }

    public static void main(String[] args) {
        EducationExpense educationExpense = new EducationExpense(3, 7030000000); );
        System.out.println("Category: " + educationExpense.getCategory());
        System.out.println("Amount: " + educationExpense.getAmount());
    }
}
