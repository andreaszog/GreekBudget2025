package gr. greekbudget.analytics;

public enum BudgetClass { SURPLUS, DEFICIT, BALANCED }

public class BudgetClassifier {

    public static BudgetClass classify(double balance, double eps) {
        if (balance > eps) return BudgetClass.SURPLUS;
        if (balance < -eps) return BudgetClass.DEFICIT;
        return BudgetClass.BALANCED;
    }
}
