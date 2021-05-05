package exceptions.network;

/**
 * This Exception is thrown when a player attempts to enter a game while its number of players has not yet been decided.
 */
public class UnknownPlayerNumberException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The number of players for the game that is currently being deployed has not yet been decided.");
    }
}
