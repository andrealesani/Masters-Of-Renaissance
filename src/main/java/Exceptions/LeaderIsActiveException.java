package Exceptions;

/**
 * This Exception is thrown when a leader card is attempted to be discarded despite being active.
 */
public class LeaderIsActiveException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("This leader card has already been activated.");
    }
}
