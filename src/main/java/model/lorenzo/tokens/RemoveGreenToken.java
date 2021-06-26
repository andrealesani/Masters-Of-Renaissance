package model.lorenzo.tokens;

import model.card.CardColor;
import model.CardTable;

/**
 * Represents the token that removes 2 green DevelopmentCards from the grid
 */
public class RemoveGreenToken extends RemoveCardsToken{
    /**
     * Constructor
     *
     * @param cardTable the game's CardTable object
     */
    public RemoveGreenToken(CardTable cardTable) {
        super(CardColor.GREEN, cardTable, LorenzoTokenType.RemoveGreen);
    }
}
