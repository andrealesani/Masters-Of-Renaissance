package model.lorenzo.tokens;

import model.CardColor;
import model.CardTable;
import network.beans.TokenType;

public class RemoveYellowToken extends RemoveCardsToken{
    public RemoveYellowToken(CardTable cardTable) {
        super(CardColor.YELLOW, cardTable, TokenType.RemoveYellow);
    }
}
