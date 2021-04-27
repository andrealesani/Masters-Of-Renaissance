package model.lorenzo.tokens;

import model.CardColor;
import model.CardTable;
import network.beans.TokenType;

public class RemovePurpleToken extends RemoveCardsToken{
    public RemovePurpleToken(CardTable cardTable) {
        super(CardColor.PURPLE, cardTable, TokenType.RemovePurple);
    }
}
