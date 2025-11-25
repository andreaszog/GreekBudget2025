public class BudgetCategory {
    private String name;
    private double allocatedAmount;
    private double spentAmount;

    public BudgetCategory(String name, double allocatedAmount) {
        this.name = name;
        this.allocatedAmount = allocatedAmount;
        this.spentAmount = 0.0;
    }

    public String getName() {
        return name;
    }

    public double getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(double allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public double getSpentAmount() {
        return spentAmount;
    }

    public void addExpense(double amount) {
        this.spentAmount += amount;
    }

    public double getRemainingAmount() {
        return allocatedAmount - spentAmount;
    }
}

