package it.polimi.ingsw.model.lorenzo.tokens;

import it.polimi.ingsw.model.CardTable;
import it.polimi.ingsw.model.card.CardColor;

/**
 * Represents the token that removes 2 yellow DevelopmentCards from the grid
 */
public class RemoveYellowToken extends RemoveCardsToken{
    /**
     * Constructor
     *
     * @param cardTable the game's CardTable object
     */
    public RemoveYellowToken(CardTable cardTable) {
        super(CardColor.YELLOW, cardTable, LorenzoTokenType.RemoveYellow);
    }
}
