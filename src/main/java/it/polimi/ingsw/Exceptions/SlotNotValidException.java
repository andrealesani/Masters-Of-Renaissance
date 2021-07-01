package it.polimi.ingsw.Exceptions;

/**
 * This Exception is thrown when the player attempts to buy a development card for a slot that cannot hold a card of that level.
 */
public class SlotNotValidException extends Exception {
    /**
     * The number of the slot
     */
    private final int slotNumber;
    /**
     * The level of the card
     */
    private final int cardLevel;

    /**
     * Constructor
     *
     * @param slotNumber the number of the card slot
     * @param cardLevel  the level of the card
     */
    public SlotNotValidException(int slotNumber, int cardLevel) {
        this.slotNumber = slotNumber;
        this.cardLevel = cardLevel;
    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return - the message (String) of this exception
     */
    @Override
    public String getMessage() {
        return ("Card slot number " + slotNumber + " cannot hold a card of level " + cardLevel + ".");
    }
}
