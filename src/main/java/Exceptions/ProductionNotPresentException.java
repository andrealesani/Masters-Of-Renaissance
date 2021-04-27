package Exceptions;

public class ProductionNotPresentException extends Exception{
    /**
     * Returns the message describing the type of error that occurred
     *
     * @return the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Error: can't find the specified production");
    }
}
