package gr.greekbudget.backend;
public class Expense {
 private String category;
 private double amount;
 public Expense(String category, double amount) {
  this.category = category;
  this.amount = amount;
 }
 public String getCategory() { return category; }
 public double getAmount() { return amount; }
 @Override
 public String toString() {
  return "Expense{" + "category='" + category + '\'' + ", amount=" + amount + '}';
 }
}
"@ > Expense.java