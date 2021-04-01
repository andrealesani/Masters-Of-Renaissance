package Exceptions;

public class RequirementsNotMetException extends Exception{
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Error: Player does not fulfill all card requirements.");
    }
}
