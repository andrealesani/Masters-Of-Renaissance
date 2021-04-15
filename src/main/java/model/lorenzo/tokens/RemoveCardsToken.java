package model.lorenzo.tokens;

import Exceptions.EmptyDeckException;
import model.CardColor;
import model.CardTable;

/**
 * Represents the token that removes 2 DevelopmentCards of the specified color from the grid
 */
public class RemoveCardsToken implements ActionToken {
    /**
     * This attribute stores the color of the development cards to discard
     */
    private final CardColor color;
    /**
     * This attribute stores the card table from which to discard the cards
     */
    private final CardTable cardTable;

    //CONSTRUCTORS

    /**
     * Constructor
     *
     * @param color     specifies the color of the cards that this token remove
     * @param cardTable reference to CardTable instance
     */
    public RemoveCardsToken(CardColor color, CardTable cardTable) {
        this.color = color;
        this.cardTable = cardTable;
    }

    //PUBLIC METHODS

    /**
     * Standard method in the interface that calls the class-specific method singleIncreaseShuffle()
     */
    public void doAction() {
        removeCardsFromCardTable();
    }

    //PRIVATE METHODS

    /**
     * Removes 2 DevelopmentCards of the specified color from the grid
     */
    private void removeCardsFromCardTable() {
        try {
            cardTable.discardTop(color);
            cardTable.discardTop(color);
        } catch (EmptyDeckException ex) {
            //Does nothing
        }
    }
}
