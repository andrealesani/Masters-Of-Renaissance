package model.lorenzo.tokens;

import model.card.CardColor;
import model.CardTable;

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
