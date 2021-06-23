package model.lorenzo.tokens;

import model.CardColor;
import model.CardTable;

/**
 * Represents the token that removes 2 purple DevelopmentCards from the grid
 */
public class RemovePurpleToken extends RemoveCardsToken{
    /**
     * Constructor
     *
     * @param cardTable the game's CardTable object
     */
    public RemovePurpleToken(CardTable cardTable) {
        super(CardColor.PURPLE, cardTable, LorenzoTokenType.RemovePurple);
    }
}
