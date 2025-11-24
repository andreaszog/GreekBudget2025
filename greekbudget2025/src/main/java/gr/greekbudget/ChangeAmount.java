public class ChangeAmount extends DemandAmount {
    public static void Simulation( String categoryChoice, String category, float newAmount) {
        if ( categoryChoice == "Έσοδα") {
            for ( int i = 0; i < RevenuesName.size(); i++ ) {
                if (RevenuesName.size(i) == category) {
                    Revenue.set(i, newAmount);
                }
            }
        } else {
            for ( int i = 0; i < ExpensesName.size(); i++ ) {
                if (ExpensesName.size(i) == category) {
                    Expenses.set(i, newAmount);
                }
            }
        }
    }
    public float newRevenues = BudgetAnalyzer.calculateTotalRevenue(Revenue);
    System.out.println ("Καινούργιο σύνολο Εσόδων:", BudgetAnalyzer.calculateTotalRevenue(Revenue));
    public float newExpenses = BudgetAnalyzer.calculateTotalExpense(Expenses);
    System.out.println ("Καινούργιο σύνολο Εξόδων:", BudgetAnalyzer.calculateTotalRevenue(Expenses));
}