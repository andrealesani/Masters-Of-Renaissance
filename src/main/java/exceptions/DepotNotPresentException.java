package Exceptions;

/**
 * This Exception is thrown when an operation is attempted on a non-existent depot by using the wrong depot number.
 */
public class DepotNotPresentException extends Exception {
    private final int depotNumber;

    /**
     * Constructor
     *
     * @param depotNumber the number of the depot that does not exist
     */
    public DepotNotPresentException(int depotNumber) {
        this.depotNumber = depotNumber;
    }

    /**
     * Returns the message describing the type of error that occurred
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Depot number " + depotNumber + " does not exist.");
    }
}
