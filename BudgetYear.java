import java.util.ArrayList;
import java.util.List;

public class BudgetYear {
    private int year;
    private List<BudgetCategory> categories;

    public BudgetYear(int year) {
        this.year = year;
        this.categories = new ArrayList<>();
    }

    public int getYear() {
        return year;
    }

    public void addCategory(BudgetCategory category) {
        categories.add(category);
    }

    public void removeCategory(String name) {
        categories.removeIf(c -> c.getName().equalsIgnoreCase(name));
    }

    public BudgetCategory getCategory(String name) {
        for (BudgetCategory c : categories) {
            if (c.getName().equalsIgnoreCase(name)) return c;
        }
        return null;
    }

    public double calculateTotalIncome() {
        // Αν χρειαστείς, μπορείς να προσθέσεις εισόδημα σε κάθε κατηγορία
        return 0.0;
    }

    public double calculateTotalExpenses() {
        double total = 0.0;
        for (BudgetCategory c : categories) {
            total += c.getSpentAmount();
        }
        return total;
    }

    public double calculateBalance() {
        double totalAllocated = 0.0;
        for (BudgetCategory c : categories) {
            totalAllocated += c.getAllocatedAmount();
        }
        return totalAllocated - calculateTotalExpenses();
    }

    public List<BudgetCategory> getCategories() {
        return categories;
    }
}
