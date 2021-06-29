package it.polimi.ingsw.model.lorenzo.tokens;

import it.polimi.ingsw.model.CardTable;
import it.polimi.ingsw.model.card.CardColor;

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
