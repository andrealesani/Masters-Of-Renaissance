package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.model.CardTable;
import it.polimi.ingsw.model.lorenzo.Lorenzo;
import it.polimi.ingsw.model.lorenzo.tokens.ActionToken;
import it.polimi.ingsw.model.lorenzo.tokens.SingleFaithShuffleToken;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SingleFaithShuffleTokenTest {

    /**
     * This method tests the token's action
     */
    @Test
    void doAction() {
        Lorenzo lorenzo = new Lorenzo(new CardTable(0), new ArrayList<>());
        ActionToken token = new SingleFaithShuffleToken(lorenzo);

        int tokenTotal = lorenzo.getActiveDeck().size();
        lorenzo.takeTurn();
        lorenzo.takeTurn();
        int previousFaith = lorenzo.getFaith();

        token.doAction();

        assertEquals(tokenTotal, lorenzo.getActiveDeck().size());
        assertEquals(previousFaith + 1, lorenzo.getFaith());

        lorenzo.takeTurn();
        lorenzo.takeTurn();
        previousFaith = lorenzo.getFaith();

        token.doAction();

        assertEquals(tokenTotal, lorenzo.getActiveDeck().size());
        assertEquals(previousFaith + 1, lorenzo.getFaith());
    }
}