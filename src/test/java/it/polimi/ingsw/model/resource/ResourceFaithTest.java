package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.PlayerBoard;
import it.polimi.ingsw.model.resource.ResourceFaith;
import it.polimi.ingsw.model.resource.ResourceServant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceFaithTest {

    @Test
    void equals() {
        assertTrue(new ResourceFaith().equals(new ResourceFaith()));
        assertFalse(new ResourceFaith().equals(new ResourceServant()));
    }

    @Test
    void addResourceFromMarket() {
        PlayerBoard playerBoard = new PlayerBoard();
        ResourceFaith faith = new ResourceFaith();

        faith.addResourceFromMarket(playerBoard);
        assertEquals(1, playerBoard.getFaith());

        faith.addResourceFromMarket(playerBoard);
        assertEquals(2, playerBoard.getFaith());
    }

    @Test
    void addResourceFromProduction() {
        PlayerBoard playerBoard = new PlayerBoard();
        ResourceFaith faith = new ResourceFaith();

        faith.addResourceFromMarket(playerBoard);
        assertEquals(1, playerBoard.getFaith());

        faith.addResourceFromMarket(playerBoard);
        assertEquals(2, playerBoard.getFaith());
    }

}