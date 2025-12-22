package gr.greekbudget.export;

import java.util.ArrayList;
import java.util.List;

public class ExportOptions {

    private int year;

    public boolean exportRevenueChart;
    public boolean exportExpenseChart;
    public boolean exportSummaryBars;
    public boolean exportPieAll;
    public boolean exportPieNoFinance;
    public boolean exportMinistryTrends;

    public List<String> selectedMinistries = new ArrayList<>();

    public ExportOptions(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public boolean hasAnySelection() {
        return exportRevenueChart
                || exportExpenseChart
                || exportSummaryBars
                || exportPieAll
                || exportPieNoFinance
                || (exportMinistryTrends && !selectedMinistries.isEmpty());
    }
}
