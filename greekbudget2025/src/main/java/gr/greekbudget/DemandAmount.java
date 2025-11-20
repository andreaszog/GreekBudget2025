import java.util.Scanner;

public class DemandAmount {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Ποιας κατηγορίας τιμή θέλετε να αλλάξετε;")
    public boolean TypeCategory = false;
    While (TypeCategory == false) {
        String category = scanner.nextLine();
        if (category == exit) {
        system.exit(0);
        }
        public boolean SuccessOrNo(boolean TypeCategory) {
            for (i = 1; i = length..., i++) {
                if (...[i] == category) {
                    TypeCategory = true;
                    return TypeCategory;
                    }
            }
        }
        if (TypeCategory == false) {
            System.out.println("Δώσατε κατηγορία που δεν υπάρχει, παρακαλώ πληκτρολογείστε ξανά.");
        }
    }
    public double newAmount;
    While (TypeAmount == false) {
        double newAmount = scanner.nextDouble();
        public boolean ValueControl (boolean TypeAmount, double newAmount) throws ArithmeticException {
            if (newAmount < 0) {
                throw new ArithmeticException("Η τιμή πρέπει να είναι μεγαλύτερη του 0, παρακαλώ ξαναδώσε τιμή.");
            } else {
                TypeAmount = true;
            }
            return TypeValue;
        }
    }
}
scanner.close();