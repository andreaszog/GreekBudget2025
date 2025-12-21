package gr.greekbudget;

public class BudgetImpactRow {

    private final int year;
    private final long before;
    private final long after;

    public BudgetImpactRow(int year, long before, long after) {
        this.year = year;
        this.before = before;
        this.after = after;
    }

    public int getYear() {
        return year;
    }

    public long getBefore() {
        return before;
    }

    public long getAfter() {
        return after;
    }

    public long getDelta() {
        return after - before;
    }

    public boolean improved() {
        return after > before;
    }
}
