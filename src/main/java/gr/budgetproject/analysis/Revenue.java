package gr.budgetproject.analysis;
public class Revenue {
 private String source;
 private double amount;
 public Revenue(String source, double amount) {
  this.source = source;
  this.amount = amount;
 }
 public String getSource() { return source; }
 public double getAmount() { return amount; }
 @Override
 public String toString() {
  return "Revenue{" + "source='" + source + '\'' + ", amount=' + amount + '}';
 }
}

