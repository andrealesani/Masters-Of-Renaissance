package model.lorenzo.tokens;

import model.CardColor;
import model.CardTable;
import network.beans.LorenzoTokenType;

public class RemoveYellowToken extends RemoveCardsToken{
    public RemoveYellowToken(CardTable cardTable) {
        super(CardColor.YELLOW, cardTable, LorenzoTokenType.RemoveYellow);
    }
}
