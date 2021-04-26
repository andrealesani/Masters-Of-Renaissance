package model.lorenzo.tokens;

import model.CardColor;
import model.CardTable;
import network.beans.TokenType;

public class RemoveBlueToken extends RemoveCardsToken{
    public RemoveBlueToken(CardTable cardTable) {
        super(CardColor.BLUE, cardTable, TokenType.RemoveBlue);
    }
}
