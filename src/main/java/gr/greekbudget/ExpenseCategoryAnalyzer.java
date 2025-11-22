package gr.greekbudget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseCategoryAnalyzer {

    public Map<String, Double> calculateCategoryTotals(List<BudgetDataModel> data) {
        Map<String, Double> categories = new HashMap<>();

        for (BudgetDataModel entry : data) {
            if (!entry.isIncome()) {
                categories.put(entry.getCategory(),
                        categories.getOrDefault(entry.getCategory(), 0.0) + entry.getAmount());
            }
        }

        return categories;

    }

    public void printCategoryPercentages(List<BudgetDataModel> data) {
        Map<String, Double> totals = calculateCategoryTotals(data);

        double totalExpenses = totals.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        
            
        System.out.println("\n===== Ποσοστά Δαπανών Ανά Κατηγορία =====\n");

        for (String category: totals.keySet()) {
            double percent = (totals.get(category) / totalExpenses) * 100;
            System.out.println("%s: %.2f%%%n", category, percent);
        }
    }
}
