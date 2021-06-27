package Exceptions;

/**
 * This Exception is thrown when the production choice is attempted to be confirmed while there are still undefined jollies in input or output.
 */
public class UndefinedJollyException extends Exception {
    /**
     * The location of the undefined jolly
     */
    private final String jollyLocation;

    /**
     * Constructor
     *
     * @param jollyLocation the location of the undefined jollies
     */
    public UndefinedJollyException(String jollyLocation) {
        this.jollyLocation = jollyLocation;
    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("There are still jolly resources in " + jollyLocation + ".");
    }
}
