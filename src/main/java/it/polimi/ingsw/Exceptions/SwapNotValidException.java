package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when two depots are attempted to be swapped, and the resources of one cannot be added to the other.
 */
public class SwapNotValidException extends Exception {
    /**
     * The number of the first depot
     */
    private final int depotNumber1;
    /**
     * The number of the second depot
     */
    private final int depotNumber2;

    /**
     * Constructor
     *
     * @param depotNumber1 the number of the first depot
     * @param depotNumber2 the number of the second depot
     */
    public SwapNotValidException(int depotNumber1, int depotNumber2) {
        this.depotNumber1 = depotNumber1;
        this.depotNumber2 = depotNumber2;
    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The contents of depots number " + depotNumber1 + " and " + depotNumber2 + " cannot be swapped.");
    }
}
