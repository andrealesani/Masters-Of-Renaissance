package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when two depots are attempted to be swapped, and the resources of one cannot be added to the other.
 */
public class SwapNotValidException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The contents of the two depots cannot be swapped.");
    }
}
