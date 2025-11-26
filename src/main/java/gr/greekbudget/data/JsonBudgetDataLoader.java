package gr.greekbudget.data;

import gr.greekbudget.model.BudgetEntry;
import gr.greekbudget.model.BudgetSector;
import gr.greekbudget.interfaces.IDataLoader;

import java.util.ArrayList;
import java.util.List;

public class JsonBudgetDataLoader implements IDataLoader {

    @Override
    public List<BudgetEntry> loadBudgetEntries(int year) {

        List<BudgetEntry> list = new ArrayList<>();

        // MOCK DATA — replace later
        list.add(new BudgetEntry(BudgetSector.DEFENCE, year, 0, 10000, 500));
        list.add(new BudgetEntry(BudgetSector.HEALTH, year, 0, 15000, 0));
        list.add(new BudgetEntry(BudgetSector.EDUCATION, year, 0, 8000, 0));
        list.add(new BudgetEntry(BudgetSector.WELFARE, year, 0, 12000, 0));
        list.add(new BudgetEntry(BudgetSector.TOURISM, year, 0, 2000, 0));
        list.add(new BudgetEntry(BudgetSector.ENVIRONMENT_ENERGY, year, 0, 4000, 0));

        return list;
    }
}
