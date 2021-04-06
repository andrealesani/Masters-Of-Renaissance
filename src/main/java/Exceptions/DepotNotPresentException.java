package Exceptions;

/**
 * This Exception is thrown when an operation is attempted on a non-existent depot by using a wrong index.
 */
public class DepotNotPresentException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Error: Depot with the given index does not exist.");
    }
}
