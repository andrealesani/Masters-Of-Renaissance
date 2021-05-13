package Exceptions;

/**
 * This Exception is thrown when the player attempts to select a leaderCard that does not exist.
 */
public class LeaderNotPresentException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("This leader card does not exist.");
    }
}
