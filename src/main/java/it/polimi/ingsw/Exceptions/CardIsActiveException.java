package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when the player attempts to activate or discard a leaderCard that is already active.
 */
public class CardIsActiveException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The selected leader card is active.");
    }
}
