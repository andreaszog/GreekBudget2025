package gr.greekbudget;

import java.util.List;

public class IncomeExpensesReport {

    public void printDetailedReport(List<BudgetDataModel> data) {
        double totalIncome = 0;
        double totalExpenses = 0;

        System.out.println("\n==== Αναλυτική Παρουσίαση Εσόδων - Εξόδων ====\n");

        for (BudgetDataModel entry : data) {
            System.out.println(entry);

            if (entry.isIncome()) {
                totalIncome += entry.getAmount();
            } else {
                totalExpenses += entry.getAmount();
            }
        }

    System.out.println("\nΣύνολο Εξόδων: " + totalIncome + "€");
    System.out.println("Σύνολο Εξόδων: " + totalExpenses + "€");
    System.out.println("Ισοζύγιο: " + (totalIncome - totalExpenses) + "€");
    }
}
