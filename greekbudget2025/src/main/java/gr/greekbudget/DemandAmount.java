package gr.greekbudget;

import java.util.Scanner;

public class DemandAmount {
    static Scanner scanner = new Scanner(System.in);
    public boolean typeCategory = false;
    public static String category;
    system.outprintln("Θέλετε να αλλάξετε έξοδα ή έσοδα και για  ποιά κατηγορία;");
    public static String categoryChoice = scanner.next();
    public static boolean SuccessOrNo(boolean typeCategory, String RevenuesName, String ExpensesName) {
        While (!typeCategory) {
            String category = scanner.next();
            if (categoryChoice == "Έσοδα") {
                for (String item : RevenuesName() ) {
                    if ( item == category) {
                        typeCategory = true;
                    }
                }
            } else {
                for (String item : ExpensesName() ) {
                    if ( item == category) {
                        typeCategory = true;
                    }
                }
            }
            if (typeCategory == false) {
                System.out.println("Δώσατε κατηγορία που δεν υπάρχει, παρακαλώ πληκτρολογείστε ξανά.");
            }
        }
        return typeCategory;
    }
    private static void While(boolean b) {
        throw new UnsupportedOperationException("Unimplemented method 'While'");
    }
    boolean typeAmount = false;
    public static float ValueControl (boolean typeAmount, float newAmount) throws ArithmeticException {
        While (!typeAmount) {
            public static float newAmount = scanner.nextFloat();
            if (newAmount < 0) {
                throw new ArithmeticException("Η τιμή πρέπει να είναι μεγαλύτερη του 0, παρακαλώ ξαναδώσε τιμή.");
            } else {
                typeAmount = true;
            }
            return newAmount;
        }
    }
}
scanner.close();
