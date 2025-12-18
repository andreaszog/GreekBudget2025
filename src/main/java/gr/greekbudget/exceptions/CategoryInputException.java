package gr.greekbudget.exceptions;
import java.util.Scanner;

public class CategoryInputException {
    public static void main (String[] args) {
        Scanner answer = null;
        try {
            System.out.println ("Πληκτρολογήστε μία κατηγορία από τις παρακάτω:
            1 = Εθνική Άμυνα
            2 = Υγεία
            3 = Παιδεία
            4 = Κοινωνικ΄γ Προστασία και Πρόνοια
            5 = Τουρισμός
            6 = Περιβάλλον και Υγεία");
            int category = answer.nextInt();
            System.out.println("Πληκτρολόγησες:" + category);
        } catch (java.util.InputMisMatchExcpetion e) {
            System.out.println("Λάθος είσοδος δεδομένων");
            if (sc != null) answer.close();
        }
        System.out.println ("Ευχαριστούμε για την συμμετοχή σας!" );
    }
}
