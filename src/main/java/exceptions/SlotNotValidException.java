package exceptions;

/**
 * This Exception is thrown when the player attempts to buy a development card for a slot that cannot hold a card of that level.
 * */
public class SlotNotValidException extends Exception {
    /**
     * Returns the message describing the type of error that occurred
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The selected slot cannot hold a card of this level.");
    }
}
