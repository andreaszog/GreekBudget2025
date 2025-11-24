public class ChangeAmount extends DemandAmount {
    public static void Simulation( String categoryChoice, String category, float newAmount) {
        if ( categoryChoice == "Έσοδα") {
            for ( int i == 0; i < RevenuesName.size(); i++ ) {
                if (item == category) {
                    Revenue.set(position, newAmount);
                }
            }
        } else {
            for ( int i == 0; i < ExpensesName.size(); i++ ) {
                if (item == category) {
                    Expenses.set(position, newAmount);
                }
            }
        }
    }
    public float newRevenues = BudgetAnalyzer.calculateTotalRevenue(Revenue);
    System.out.println ("Καινούργιο σύνολο Εσόδων:", BudgetAnalyzer.calculateTotalRevenue(Revenue));
    public float newExpenses = BudgetAnalyzer.calculateTotalExpense(Expenses);
    System.out.println ("Καινούργιο σύνολο Εξόδων:", BudgetAnalyzer.calculateTotalRevenue(Expenses));
}