package it.polimi.ingsw.model.lorenzo;

import it.polimi.ingsw.model.CardTable;
import it.polimi.ingsw.model.lorenzo.Lorenzo;
import it.polimi.ingsw.model.lorenzo.tokens.ActionToken;
import it.polimi.ingsw.model.lorenzo.tokens.DoubleFaithToken;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DoubleFaithTokenTest {
    /**
     * This method tests the token's action
     */
    @Test
    void doAction() {
        Lorenzo lorenzo = new Lorenzo(new CardTable(0), new ArrayList<>());
        ActionToken token = new DoubleFaithToken(lorenzo);

        token.doAction();

        assertEquals(2, lorenzo.getFaith());
    }
}