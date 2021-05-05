package exceptions;

/**
 * This Exception is thrown when a resource is attempted to be inserted in a storage that is at full capacity.
 */
public class NotEnoughSpaceException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("There is no space left in storage for this type of resource.");
    }
}