package gr.greekbudget.analytics;

import gr.greekbudget.interfaces.IBudgetCalculator;
import gr.greekbudget.model.BudgetEntry;
import java.util.List;

public abstract class BaseCalculator implements IBudgetCalculator {
    @Override
    public abstract double calculate(List<BudgetEntry> entries);
}

