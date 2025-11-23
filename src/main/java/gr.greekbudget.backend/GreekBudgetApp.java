package gr.greekbudget.backend;

import java.util.ArrayList;
import java.util.List;

public class GreekBudgetApp {

    public static void main(String[] args) {
        System.out.println("GreekBudget Backend running...");

        // Δημιουργία λίστας εσόδων
        List<Revenue> revenues = new ArrayList<>();
        revenues.add(new Revenue("Φόροι", 45000));
        revenues.add(new Revenue("Επιχορηγήσεις ΕΕ", 12500));
        revenues.add(new Revenue("Μη φορολογικά", 7320));

        // Εκτύπωση λίστας εσόδων
        System.out.println("Έσοδα:");
        for (Revenue r : revenues) {
            System.out.println(r.getCategory() + ": " + r.getAmount() + " €");
        }

        // Δημιουργία λίστας εξόδων
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("Υγεία", 28000));
        expenses.add(new Expense("Παιδεία", 18500));
        expenses.add(new Expense("Συντάξεις", 30200));

        // Εκτύπωση λίστας εξόδων
        System.out.println("\nΈξοδα:");
        for (Expense e : expenses) {
            System.out.println(e.getCategory() + ": " + e.getAmount() + " €");
        }

        // Υπολογισμός ισοζυγίου
        double totalRevenue = revenues.stream().mapToDouble(Revenue::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double balance = totalRevenue - totalExpense;

        System.out.println("\nΙσοζύγιο: " + balance + " €");
        System.out.println("Χαρακτηρισμός Προϋπολογισμού: " + (balance >= 0 ? "Πλεόνασμα" : "Έλλειμμα"));
    }
}

