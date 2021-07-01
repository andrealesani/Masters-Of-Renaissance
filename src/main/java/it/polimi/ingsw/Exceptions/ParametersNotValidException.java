package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when an operation is attempted using input values that aren't allowed.
 */
public class ParametersNotValidException extends RuntimeException {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The selected input values are not allowed.");
    }
}
