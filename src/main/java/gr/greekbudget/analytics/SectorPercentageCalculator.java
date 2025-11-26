package gr.greekbudget.analytics;

import gr.greekbudget.model.BudgetEntry;
import gr.greekbudget.model.BudgetSector;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class SectorPercentageCalculator {

    public Map<BudgetSector, Double> calculatePercentages(List<BudgetEntry> entries) {

        EnumMap<BudgetSector, Double> totals = new EnumMap<>(BudgetSector.class);

        double total = 0;

        for (BudgetEntry b : entries) {
            totals.put(
                b.getSector(),
                totals.getOrDefault(b.getSector(), 0.0) + b.getExpense()
            );
            total += b.getExpense();
        }

        for (BudgetSector s : totals.keySet()) {
            totals.put(s, (totals.get(s) / total) * 100);
        }

        return totals;
    }
}
