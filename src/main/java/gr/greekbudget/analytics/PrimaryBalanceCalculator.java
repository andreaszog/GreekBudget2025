package gr.greekbudget.analytics;

import gr.greekbudget.model.BudgetEntry;
import java.util.List;

public class PrimaryBalanceCalculator extends BaseCalculator {

    @Override
    public double calculate(List<BudgetEntry> entries) {

        double revenue = 0;
        double expenses = 0;
        double interest = 0;

        for (BudgetEntry e : entries) {
            revenue += e.getRevenue();
            expenses += e.getExpense();
            interest += e.getInterest();
        }

        return revenue - (expenses - interest);
    }
}
