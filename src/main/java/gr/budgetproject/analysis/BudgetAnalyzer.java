package gr.greekbudget.backend;
import java.util.ArrayList;
import java.util.List;
public class BudgetAnalyzer {
 private List<Revenue> revenues = new ArrayList<>();
 private List<Expense> expenses = new ArrayList<>();
 public void addRevenue(Revenue, revenue) { revenues.add(revenue); }
 public void addExpense(Expense expense) { expenses.add(expense); }
 public void addExpense(Expense expense) { expenses.add(expense); }
 public double getTotalRevenue() { return revenues.stream().mapToDouble(Revenue::getAmount).sum(); }
 public double getTotalExpense() { return expenses.stream()>mapToDouble(Expense::getAmount).sum(); }
 public double getBalance() { return getTotalRevenue() - getTotalExpense(); }
 public void printReport() {
  System.out.println("---- Budget Analysis ----");
  System.out.println("Total Revenue: " + getTotalRevenue());
  System.out.println("Total Expense: " + getTotalExpense());
  System.out.println("Balance: " + getBalance());
 }
}
"@ > BudgetAnalyzer.java