package model.lorenzo;

import Exceptions.EmptyDeckException;
import model.card.CardColor;
import model.CardTable;
import model.lorenzo.tokens.ActionToken;
import model.lorenzo.tokens.RemoveYellowToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveCardsTokenTest {
    /**
     * This method tests the token's action
     */
    @Test
    void doAction() throws EmptyDeckException {
        CardTable cardTable = new CardTable(0);
        ActionToken token = new RemoveYellowToken(cardTable);

        int levelOneNumber = cardTable.getYellowCards().get(2).size();
        int levelTwoNumber = cardTable.getYellowCards().get(1).size();
        int levelThreeNumber = cardTable.getYellowCards().get(0).size();

        token.doAction();

        assertEquals(levelOneNumber-2, cardTable.getYellowCards().get(0).size());

        token.doAction();
        token.doAction();

        assertEquals(levelOneNumber-4, cardTable.getYellowCards().get(0).size());
        assertEquals(levelTwoNumber-2, cardTable.getYellowCards().get(1).size());

        cardTable.discardTop(CardColor.YELLOW);

        token.doAction();

        assertEquals(levelTwoNumber-4, cardTable.getYellowCards().get(1).size());
        assertEquals(levelThreeNumber-1, cardTable.getYellowCards().get(2).size());
    }
}