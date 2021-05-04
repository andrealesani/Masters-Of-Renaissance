package model.lorenzo.tokens;

import model.CardColor;
import model.CardTable;
import network.beans.LorenzoTokenType;

public class RemoveGreenToken extends RemoveCardsToken{
    public RemoveGreenToken(CardTable cardTable) {
        super(CardColor.GREEN, cardTable, LorenzoTokenType.RemoveGreen);
    }
}
