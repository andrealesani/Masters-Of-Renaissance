package it.polimi.ingsw.model.lorenzo.tokens;

import it.polimi.ingsw.model.CardTable;
import it.polimi.ingsw.model.card.CardColor;

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
