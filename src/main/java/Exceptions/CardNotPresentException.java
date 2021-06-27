package Exceptions;

/**
 * This Exception is thrown when an operation is attempted on a development card that could not be found
 */
public class CardNotPresentException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The specified DevelopmentCard does not exist");
    }
}
