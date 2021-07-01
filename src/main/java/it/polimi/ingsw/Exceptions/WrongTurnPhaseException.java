package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when the player attempts an action not allowed during the current phase of the turn.
 */
public class WrongTurnPhaseException extends Exception {
    /**
     * The specifics of why the exception was thrown
     */
    private final String specifics;

    /**
     * Constructor for when specifics are provided regarding what caused the exception
     *
     * @param specifics the specifics on the cause
     */
    public WrongTurnPhaseException(String specifics) {
        this.specifics = specifics;
    }

    /**
     * Constructor for when no specifics are provided regarding what caused the exception
     */
    public WrongTurnPhaseException() {
        this.specifics = null;
    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        if (specifics == null)
            return ("This action is not allowed during this phase of the turn.");
        return ("This action is not allowed during this phase of the turn: " + specifics);
    }
}
