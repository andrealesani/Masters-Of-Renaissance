package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when the player cancels a command they are writing in the CLI.
 */
public class OperationCancelledException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Operation cancelled.");
    }
}
