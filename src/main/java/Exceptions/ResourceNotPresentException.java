package Exceptions;

/**
 * This Exception is thrown when an operation is attempted on a resource which is not available to the method that throws it.
 */
public class ResourceNotPresentException extends Exception {
    /**
     * Returns the message describing the type of error that occured.
     * @return - the message (String) of this exception.
     */
    @Override
    public String getMessage() {
        return ("Error: Resource is not present.");
    }
}
