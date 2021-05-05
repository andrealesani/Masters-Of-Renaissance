package Exceptions.network;

/**
 * This Exception is thrown when a player attempts to login with a username already taken by another player.
 */
public class UsernameAlreadyExistsException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The selected username already exists.");
    }
}
