package gr.greekbudget.backend;

public class ProtectionAndWerlfareExpense {

    public ProtectionAndWerlfareExpense(int category, double amount) {
        super(category, amount);
    }

    public static void main(String[] args) {
        HealthExpense healthExpense = new HealthExpense(4, 45059194); 
        System.out.println("Category: " + healthExpense.getCategory());
        System.out.println("Amount: " + healthExpense.getAmount());
    }
    }

    
}
