package model.lorenzo.tokens;

import model.CardColor;
import model.CardTable;
import network.beans.TokenType;

public class RemoveGreenToken extends RemoveCardsToken{
    public RemoveGreenToken(CardTable cardTable) {
        super(CardColor.GREEN, cardTable, TokenType.RemoveGreen);
    }
}
