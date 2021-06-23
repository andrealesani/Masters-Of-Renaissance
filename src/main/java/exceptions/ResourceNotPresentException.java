package Exceptions;

/**
 * This Exception is thrown when an operation is attempted on a resource which is not present in the object that throws the exception.
 */
public class ResourceNotPresentException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Resource is not present.");
    }
}
