package model.lorenzo.tokens;

import model.CardColor;
import model.CardTable;
import network.beans.LorenzoTokenType;

public class RemoveBlueToken extends RemoveCardsToken{
    public RemoveBlueToken(CardTable cardTable) {
        super(CardColor.BLUE, cardTable, LorenzoTokenType.RemoveBlue);
    }
}
