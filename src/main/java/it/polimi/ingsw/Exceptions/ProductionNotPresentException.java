package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when an operation is attempted on a non-existent production by using the wrong production number.
 */
public class ProductionNotPresentException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The requested production does not exist");
    }
}
