import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetController {
    private Map<Integer, BudgetYear> years;

    public BudgetController() {
        this.years = new HashMap<>();
    }

    public void addBudgetYear(BudgetYear year) {
        years.put(year.getYear(), year);
    }

    public BudgetYear getBudgetYear(int year) {
        return years.get(year);
    }

    public void removeBudgetYear(int year) {
        years.remove(year);
    }

    public List<Integer> listAllYears() {
        return new ArrayList<>(years.keySet());
    }

    public List<BudgetYear> listAllBudgetYears() {
        return new ArrayList<>(years.values());
    }
}

