package model.lorenzo.tokens;

import model.CardColor;
import model.CardTable;

/**
 * Represents the token that removes 2 blue DevelopmentCards from the grid
 */
public class RemoveBlueToken extends RemoveCardsToken{
    /**
     * Constructor
     *
     * @param cardTable the game's CardTable object
     */
    public RemoveBlueToken(CardTable cardTable) {
        super(CardColor.BLUE, cardTable, LorenzoTokenType.RemoveBlue);
    }
}
