package Exceptions;

/**
 * This Exception is thrown when a resource is tried to be inserted in a storage that can't contain its type.
 */
public class WrongResourceTypeException extends Exception {
    /**
     * Returns the message describing the type of error that occured
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Error: This type of resource cannot be inserted into this storage.");
    }
}
