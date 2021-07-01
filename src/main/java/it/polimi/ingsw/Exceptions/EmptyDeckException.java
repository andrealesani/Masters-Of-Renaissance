package it.polimi.ingsw.Exceptions;

import it.polimi.ingsw.model.card.CardColor;

/**
 * This Exception is thrown when an operation is attempted on an empty card table deck.
 */
public class EmptyDeckException extends Exception{
    /**
     * The color of the deck
     */
    private final CardColor color;
    /**
     * The level of the cards in the deck
     */
    private final int level;

    /**
     * Constructor for the deck of a specific level
     *
     * @param color the color of the deck
     * @param level the level of the cards in the deck
     */
    public EmptyDeckException(CardColor color, int level) {
        this.color = color;
        this.level = level;
    }

    /**
     * Constructor for all decks of a given color
     *
     * @param color the color of the decks
     */
    public EmptyDeckException(CardColor color) {
        this.color = color;
        this.level = -1;
    }

    /**
     * Returns the message describing the type of error that occurred
     *
     * @return the message (String) of this exception
     */
    @Override
    public String getMessage() {
        if (level == -1)
            return ("All " + color + " decks are empty.");

        return ("The " + color + " deck of level " + level + " is empty.");
    }
}
