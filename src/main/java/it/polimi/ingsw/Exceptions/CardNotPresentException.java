package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when an operation is attempted on a development card that could not be found
 */
public class CardNotPresentException extends Exception {
    /**
     * The card's id
     */
    private final int id;

    /**
     * Constructor
     *
     * @param id the card's id
     */
    public CardNotPresentException(int id) {
        this.id = id;

    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("The development card with id " + id + " does not exist");
    }
}
