package Exceptions;

/**
 * This Exception is thrown when an operation between two depots is attempted giving as targets the same depot twice.
 */
public class SameDepotException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Error: The selected depots are the same.");
    }
}
