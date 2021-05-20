package model.lorenzo.tokens;

import model.CardColor;
import model.CardTable;

public class RemoveYellowToken extends RemoveCardsToken{
    public RemoveYellowToken(CardTable cardTable) {
        super(CardColor.YELLOW, cardTable, LorenzoTokenType.RemoveYellow);
    }
}
