package Exceptions;

/**
 * This Exception is thrown when a resource is tried to be inserted in a storage that can't accept it because a different one is blocking it.
 */
public class BlockedResourceException extends Exception{
    /**
     * Returns the message describing the type of error that occurred
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Error: Resource can't be added to depot because it is blocked by a different one.");
    }
}
