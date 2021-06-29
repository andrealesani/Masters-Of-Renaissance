package it.polimi.ingsw.Exceptions.network;

/**
 * This Exception is thrown when a player attempts to enter a game that is already full.
 */
public class GameFullException extends Exception{
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The current game has already reached maximum player capacity.");
    }
}
