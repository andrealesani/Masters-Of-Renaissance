package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when a leader card is attempted to be activated despite the player not meeting its requirements.
 */
public class LeaderRequirementsNotMetException extends Exception{
    /**
     * The number of the leader card
     */
    private final int number;

    /**
     * Constructor
     *
     * @param number the number of the leader card
     */
    public LeaderRequirementsNotMetException(int number) {
        this.number = number;
    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The requirements for activating leader card number " + number + " are not met.");
    }
}
