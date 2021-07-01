package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when the player attempts to select a leaderCard that does not exist.
 */
public class LeaderNotPresentException extends Exception {
    /**
     * The number of the leader card
     */
    private final int number;

    /**
     * Constructor
     *
     * @param number the number of the leader card
     */
    public LeaderNotPresentException(int number) {
        this.number = number;
    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Leader card number " + number + " does not exist.");
    }
}
