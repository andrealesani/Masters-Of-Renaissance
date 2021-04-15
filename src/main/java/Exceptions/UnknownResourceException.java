package Exceptions;

public class UnknownResourceException extends Exception{
    private String unknownResourceLocation = null;

    public UnknownResourceException(String unknownResourceLocation) {
        this.unknownResourceLocation = unknownResourceLocation;
    }

    /**
     * Returns the message describing the type of error that occurred
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Error: There are still undefined resources in " + unknownResourceLocation + ".");
    }
}
