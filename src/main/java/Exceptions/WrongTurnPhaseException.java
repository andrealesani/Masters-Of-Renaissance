package Exceptions;

/**
 * This Exception is thrown when the player attempts an action not allowed during the current phase of the turn.
 */
public class WrongTurnPhaseException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("This action is not allowed during this phase of the turn.");
    }
}
