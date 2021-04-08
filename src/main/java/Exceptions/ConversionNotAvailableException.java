package Exceptions;

/**
 * This Exception is thrown when a white orb is asked to be converted into a resource for which a conversion is not available
 */
public class ConversionNotAvailableException extends Exception{
    /**
     * Returns the message describing the type of error that occurred
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Error: This conversion is not available.");
    }
}
