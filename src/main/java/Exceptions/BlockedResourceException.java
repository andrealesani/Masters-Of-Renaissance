package Exceptions;

/**
 * This Exception is thrown when it is tried to add a resource to a depot that can't accept it because a different depot is blocking it.
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
