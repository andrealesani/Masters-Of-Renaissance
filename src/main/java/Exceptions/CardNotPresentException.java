package Exceptions;

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
