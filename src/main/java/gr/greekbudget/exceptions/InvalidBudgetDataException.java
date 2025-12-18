package gr.greekbudget.exceptions;

public class InvalidBudgetDataException extends RuntimeException {
    public InvalidBudgetDataException(String msg) {
        super(msg);
    }
}
