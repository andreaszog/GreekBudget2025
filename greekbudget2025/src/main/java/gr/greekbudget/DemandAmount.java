import java.util.Scanner;

public class DemandAmount {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Ποιας κατηγορίας τιμή θέλετε να αλλάξετε;")
    public static boolean typeCategory = false;
    While (typeCategory == false) {
        String category = scanner.nextLine();
        if (category == "exit") {
        system.exit(0);
        }
        system.outprintln("Θέλετε να αλλάξετε έξοδα ή έσοδα για αυτή την κατηγορία;")
        public static String categoryChoice;
        String categoryChoice = scanner.nextString();
        if (categoryChoice == "Έσοδα") {
            public static boolean SuccessOrNo(boolean typeCategory) {
                for (String item : RevenuesName() ) {
                    if ( item == category) {
                        typeCategory = true;
                        return typeCategory;
                    }
                }
            }
        } else {
            public static boolean SuccessOrNo(boolean typeCategory) {
                for (String item : ExpensesName() ) {
                    if ( item == category) {
                        typeCategory = true;
                        return typeCategory;
                    }
                }
            }
        }
        if (typeCategory == false) {
            System.out.println("Δώσατε κατηγορία που δεν υπάρχει, παρακαλώ πληκτρολογείστε ξανά.");
        }
    }
    public static float newAmount;
    While (typeAmount == false) {
        float newAmount = scanner.nextFloat();
        public boolean ValueControl (boolean typeAmount, float newAmount) throws ArithmeticException {
            if (newAmount < 0) {
                throw new ArithmeticException("Η τιμή πρέπει να είναι μεγαλύτερη του 0, παρακαλώ ξαναδώσε τιμή.");
            } else {
                typeAmount = true;
            }
            return typeAmount;
        }
    }
}
scanner.close();