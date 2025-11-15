@"
package gr.budgetproject.analysis;
public class Main {
 public static void main(String[] args) {
  BudgetAnalyzer analyzer = new BudgetAnalyzer();
  analyzer.addRevenue(new Revenue("Φόροι", 1500000))
΄ analyzer.addRevenue(new Revenue("Επιδοτήσεις ΕΕ". 500000);
  analyzer.addExpense(new Expense("Υγεία", 600000));
  analyzer.addExpense(new Expense("Παιδεία", 400000));
  analyzer.addExpense(new Expense("Άμυνα", 300000));
  analyzer.printReport();
 }
}
"@ > Main.java