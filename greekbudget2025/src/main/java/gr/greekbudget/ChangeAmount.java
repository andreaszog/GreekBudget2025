public class ChangeAmount extends DemandAmount {
    int previousamount = 
    public static float Simulation( String categoryChoice, String category,
         float newAmount, String RevenuesName , String ExpensesName, float Revenue, float Expenses) {
        if ( categoryChoice == "Έσοδα") {
            for ( int i = 0; i < RevenuesName.size(); i++ ) {
                if (RevenuesName.size(i) == category) {
                    public float previousamount = Revenue(i);
                    Revenue.set(i, newAmount);
                }
            }
        } else {
            for ( int i = 0; i < ExpensesName.size(); i++ ) {
                if (ExpensesName.size(i) == category) {
                    public float previousamount = Expenses(i);
                    Expenses.set(i, newAmount);
                }
            }
        }
        return previousamount;
    }
    public float newRevenues = BudgetAnalyzer.calculateTotalRevenue(Revenue);
    System.out.println ("Καινούργιο σύνολο Εσόδων:", BudgetAnalyzer.calculateTotalRevenue(Revenue));
    public float newExpenses = BudgetAnalyzer.calculateTotalExpense(Expenses)
    System.out.println ("Καινούργιο σύνολο Εξόδων:", BudgetAnalyzer.calculateTotalRevenue(Expenses));
}