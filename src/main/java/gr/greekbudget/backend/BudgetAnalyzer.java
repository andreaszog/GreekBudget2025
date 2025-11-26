package gr.greekbudget.backend;

import java.util.List;

public class BudgetAnalyzer {
    
    private List<Revenue> revenues;
    private List<Expense> expenses;

    public BudgetAnalyzer(List<Revenue> revenues, List<Expense> expenses) {
        this.revenues = revenues;
        this.expenses = expenses;
    }

    public void printSummary() {
        double totalRevenue = revenues.stream().mapToDouble(Revenue::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double balance = totalRevenue - totalExpense;

        System.out.println("Σύνολο Εσόδων: " + totalRevenue + " €");
        System.out.println("Σύνολο Εξόδων: " + totalExpense + " €");
        System.out.println("Ισοζύγιο: " + balance + " €");
        System.out.println("Χαρακτηρισμός Προϋπολογισμού: " + (balance >= 0 ? "Πλεόνασμα" : "Έλλειμμα"));
    }
}
