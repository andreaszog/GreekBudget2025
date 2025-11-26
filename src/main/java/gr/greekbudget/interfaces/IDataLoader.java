package gr.greekbudget.interfaces;

import gr.greekbudget.model.BudgetEntry;
import java.util.List;

public interface IDataLoader {
    List<BudgetEntry> loadBudgetEntries(int year);
}
