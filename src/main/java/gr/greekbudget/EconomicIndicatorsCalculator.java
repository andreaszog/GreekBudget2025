package gr.greekbudget;

import java.util.List;

public class EconomicIndicatorsCalculator {

    public double calculateSurplus(List<BudgetDataModel> data) {
        double income = 0;
        double expenses = 0;

        for (BudgetDataModel entry : data) {
            if (entry.isIncome()) {
                income += entry.getAmount();
            } else {
                expenses += entry.getAmount();
            }
        }

        return income - expenses;
    }
    
    public double spendingToIncomeRatio(List<BudgetDataModel> data) {
        double income = 0;
        double expenses = 0;

        for (BudgetDataModel entry : data) {
            if (entry.isIncome()) {
                income += entry.getAmount();
            } else {
                expenses += entry.getAmount();
            }
        }

        if (income == 0) {
            return 0; // Avoid division by zero
        }

        return (expenses / income) * 100; // Return ratio as a percentage
    }

    public void printIndicators(List<BudgetDataModel> data) {

        System.out.println("\n===== Οικονομικοί Δείκτες =====\n");

        double surplus = calculateSurplus(data);
        double ratio = spendingToIncomeRatio(data);

        System.out.println("Πλεόνασμα / Έλλειμμα: " + surplus + "€");
        System.out.printf("Αναλογία Δαπανών προς Έσοδα: %.2f%%%n", ratio);
    }
}
