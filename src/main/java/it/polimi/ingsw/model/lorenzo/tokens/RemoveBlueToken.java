package it.polimi.ingsw.model.lorenzo.tokens;

import it.polimi.ingsw.model.CardTable;
import it.polimi.ingsw.model.card.CardColor;

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
