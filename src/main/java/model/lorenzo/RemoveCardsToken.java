package model.lorenzo;

import model.CardColor;
import model.CardTable;

/**
 * Represents the token that removes 2 DevelopmentCards of the specified color from the grid
 */
public class RemoveCardsToken implements ActionToken {
    private final Lorenzo lorenzo;
    private final CardColor color;
    private final CardTable cardTable;

    /**
     * Constructor
     *
     * @param lorenzo   reference to Lorenzo instance
     * @param color     specifies the color of the cards that this token remove
     * @param cardTable reference to CardTable instance
     */
    public RemoveCardsToken(Lorenzo lorenzo, CardColor color, CardTable cardTable) {
        this.lorenzo = lorenzo;
        this.color = color;
        this.cardTable = cardTable;
    }

    /**
     * Removes 2 DevelopmentCards of the specified color from the grid
     */
    private void removeCardsFromCardTable() {
        //TODO
    }

    /**
     * Standard method in the interface that calls the class-specific method singleIncreaseShuffle()
     */
    public void doAction() {
        removeCardsFromCardTable();
    }
}
