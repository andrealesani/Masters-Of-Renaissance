package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when an operation is attempted on a non-existent production by using the wrong production number.
 */
public class ProductionNotPresentException extends Exception {
    /**
     * The number of the missing production
     */
    private final int number;

    /**
     * Constructor
     *
     * @param number the number of the production that does not exist
     */
    public ProductionNotPresentException(int number) {
        this.number = number;
    }

    /**
     * Constructor
     */
    public ProductionNotPresentException() {
        this.number = -1;
    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return the message (String) of this exception
     */
    @Override
    public String getMessage() {
        if (number == -1)
            return ("The selected production does not exist.");
        return ("Production number " + number + " does not exist.");
    }
}
