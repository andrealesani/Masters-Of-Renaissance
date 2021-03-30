package Exceptions;

/**
 * This Exception is thrown when an operation is attempted that is present in the object, but not in a sufficient quantity for the required operation.
 */
public class NotEnoughResourceException extends Exception {
    /**
     * Returns the message describing the type of error that occured
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Error: Resource is not present in sufficient quantity.");
    }
}