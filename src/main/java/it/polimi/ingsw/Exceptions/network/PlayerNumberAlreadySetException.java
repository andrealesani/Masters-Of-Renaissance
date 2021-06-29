package it.polimi.ingsw.Exceptions.network;

/**
 * This Exception is thrown when a player attempts to set the player number for a game for which it has already been set.
 */
public class PlayerNumberAlreadySetException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The number of players for the game has already been set.");
    }
}
