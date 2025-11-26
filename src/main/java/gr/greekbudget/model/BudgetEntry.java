package gr.greekbudget.model;

public class BudgetEntry {

    private BudgetSector sector;
    private int year;
    private double revenue;
    private double expense;
    private double interest;

    public BudgetEntry(BudgetSector sector, int year, double revenue, double expense, double interest) {
        this.sector = sector;
        this.year = year;
        this.revenue = revenue;
        this.expense = expense;
        this.interest = interest;
    }

    public BudgetSector getSector() { return sector; }
    public int getYear() { return year; }
    public double getRevenue() { return revenue; }
    public double getExpense() { return expense; }
    public double getInterest() { return interest; }
}
